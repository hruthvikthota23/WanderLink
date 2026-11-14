import React, { useState, useEffect } from 'react';
import { useSearchParams, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useToast } from '../context/ToastContext';
import { tripService } from '../services/tripService';
import { matchingService } from '../services/matchingService';
import { connectionService } from '../services/connectionService';
import LoadingSpinner from '../components/LoadingSpinner';
import Modal from '../components/Modal';
import { 
  Users, MapPin, Calendar, Heart, Shield, Sparkles, 
  UserPlus, CheckCircle2, MessageSquare, ChevronDown, Info, Send 
} from 'lucide-react';

const BuddyMatches = () => {
  const [searchParams] = useSearchParams();
  const tripIdParam = searchParams.get('tripId');

  const { user } = useAuth();
  const { showToast } = useToast();

  const [trips, setTrips] = useState([]);
  const [selectedTripId, setSelectedTripId] = useState(tripIdParam || '');
  const [matches, setMatches] = useState([]);
  const [loading, setLoading] = useState(true);
  const [calculating, setCalculating] = useState(false);

  // Connect Modal state
  const [connectModalOpen, setConnectModalOpen] = useState(false);
  const [selectedCandidate, setSelectedCandidate] = useState(null);
  const [connectMessage, setConnectMessage] = useState('');
  const [sendingRequest, setSendingRequest] = useState(false);

  // Load user trips on mount
  useEffect(() => {
    const loadTrips = async () => {
      try {
        setLoading(true);
        if (user?.id) {
          const userTrips = await tripService.getUserTrips(user.id);
          setTrips(userTrips);

          if (userTrips.length > 0) {
            const initialId = tripIdParam || userTrips[0].id.toString();
            setSelectedTripId(initialId);
          } else {
            // Fallback: load all trips to allow demo matching if user has no trips yet
            const allTrips = await tripService.getAllTrips();
            if (allTrips.length > 0) {
              setTrips(allTrips);
              setSelectedTripId(allTrips[0].id.toString());
            }
          }
        } else {
          // If public guest preview, load all trips
          const allTrips = await tripService.getAllTrips();
          setTrips(allTrips);
          if (allTrips.length > 0) {
            setSelectedTripId(allTrips[0].id.toString());
          }
        }
      } catch (err) {
        console.error('Error loading trips:', err);
      } finally {
        setLoading(false);
      }
    };

    loadTrips();
  }, [user, tripIdParam]);

  // Load matches when selectedTripId changes
  useEffect(() => {
    if (!selectedTripId) return;

    let active = true;
    let initialLoad = true;
    const fetchMatches = async () => {
      try {
        if (initialLoad) setCalculating(true);
        const matchResults = await matchingService.getMatchesForTrip(selectedTripId);
        if (active) setMatches(matchResults);
      } catch (err) {
        console.error('Error calculating buddy matches:', err);
        if (active && initialLoad) showToast(err.message || 'Could not calculate matches', 'error');
      } finally {
        if (active && initialLoad) setCalculating(false);
        initialLoad = false;
      }
    };

    fetchMatches();
    const intervalId = window.setInterval(fetchMatches, 15000);
    return () => {
      active = false;
      window.clearInterval(intervalId);
    };
  }, [selectedTripId]);

  const handleOpenConnectModal = (candidate) => {
    setSelectedCandidate(candidate);
    setConnectMessage(`Hi ${candidate.traveler?.fullName?.split(' ')[0]}! I noticed our travel dates and interests align on WanderLink. Would love to connect and share tips!`);
    setConnectModalOpen(true);
  };

  const handleSendConnection = async (e) => {
    e.preventDefault();
    if (!selectedCandidate) return;

    try {
      setSendingRequest(true);
      await connectionService.sendConnection(selectedCandidate.traveler.id, connectMessage);
      showToast(`Connection request sent to ${selectedCandidate.traveler.fullName}!`, 'success');

      // Update candidate's connectionStatus in local state
      setMatches((prev) =>
        prev.map((m) =>
          m.traveler.id === selectedCandidate.traveler.id
            ? { ...m, connectionStatus: 'PENDING' }
            : m
        )
      );

      setConnectModalOpen(false);
    } catch (err) {
      showToast(err.message || 'Failed to send request', 'error');
    } finally {
      setSendingRequest(false);
    }
  };

  if (loading) {
    return <LoadingSpinner text="Loading trips and travel companions..." />;
  }

  const activeTrip = trips.find((t) => t.id.toString() === selectedTripId.toString());

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      {/* Header and Trip Selection Bar */}
      <div className="bg-white rounded-3xl p-6 sm:p-8 border border-slate-100 shadow-sm flex flex-col md:flex-row md:items-center justify-between gap-6">
        <div className="space-y-1">
          <div className="inline-flex items-center space-x-2 text-xs font-bold text-emerald-800 uppercase tracking-widest bg-emerald-50 px-3 py-1 rounded-full">
            <Sparkles className="w-3.5 h-3.5 text-emerald-600" />
            <span>AI Travel Companion Matching</span>
          </div>
          <h1 className="text-2xl sm:text-3xl font-extrabold text-slate-900 tracking-tight">
            Find Your Travel Buddy
          </h1>
          <p className="text-xs sm:text-sm text-slate-500">
            Intelligent weighted formula: 30% Destination • 25% Dates • 20% Interests • 15% Budget • 10% Style
          </p>
        </div>

        {/* Trip Selector Dropdown */}
        <div className="space-y-1 sm:w-80">
          <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider">
            Match Candidates For:
          </label>
          <div className="relative">
            <select
              value={selectedTripId}
              onChange={(e) => setSelectedTripId(e.target.value)}
              className="w-full px-4 py-2.5 rounded-xl border border-slate-200 bg-slate-50 text-slate-800 font-semibold text-sm outline-none focus:ring-2 focus:ring-emerald-600/20"
            >
              {trips.map((t) => (
                <option key={t.id} value={t.id}>
                  {t.destination} ({t.startDate} - {t.endDate})
                </option>
              ))}
            </select>
          </div>
        </div>
      </div>

      {/* Safety & Algorithm Disclaimer Notice */}
      <div className="p-4 rounded-2xl bg-amber-50/70 border border-amber-200/80 flex items-start space-x-3 text-xs text-amber-900">
        <Info className="w-5 h-5 text-amber-600 flex-shrink-0 mt-0.5" />
        <div>
          <strong className="font-bold">Algorithmic Compatibility Notice:</strong>
          <span className="ml-1 text-amber-800">
            The compatibility percentage is calculated strictly from declared preferences, dates, budgets and styles. 
            It is an exploratory social discovery tool and never constitutes a background or identity safety guarantee.
          </span>
        </div>
      </div>

      {/* Matches List */}
      {calculating ? (
        <LoadingSpinner text="Calculating compatibility scores against registered travelers..." />
      ) : matches.length === 0 ? (
        <div className="p-12 text-center bg-white rounded-3xl border border-slate-100 shadow-sm space-y-3">
          <Users className="w-12 h-12 text-slate-300 mx-auto" />
          <h3 className="text-lg font-bold text-slate-800">No matching travelers found yet</h3>
          <p className="text-xs sm:text-sm text-slate-500 max-w-md mx-auto">
            Try adjusting your trip dates or explore our Goa demonstration scenario where multiple travelers have matching schedules!
          </p>
          <Link
            to="/create-trip"
            className="inline-flex items-center space-x-2 px-5 py-2.5 rounded-xl bg-emerald-700 text-white font-semibold text-xs shadow-md hover:bg-emerald-800"
          >
            <span>Plan Another Trip</span>
          </Link>
        </div>
      ) : (
        <div className="space-y-6">
          <div className="flex items-center justify-between text-xs font-bold text-slate-500 uppercase tracking-wider px-2">
            <span>Ranked Candidates ({matches.length})</span>
            <span>Sorted by Highest Compatibility Score</span>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {matches.map((match, idx) => {
              const traveler = match.traveler;
              const trip = match.trip;
              const score = match.compatibilityScore;

              return (
                <div
                  key={idx}
                  className="bg-white rounded-3xl p-6 border border-slate-100 shadow-sm hover:shadow-xl transition-all space-y-5 flex flex-col justify-between relative overflow-hidden"
                >
                  {/* Top Bar: Profile & Score Badge */}
                  <div className="flex items-start justify-between gap-4">
                    <div className="flex items-center space-x-3.5">
                      <img
                        src={traveler?.avatarUrl || "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&auto=format&fit=crop&q=80"}
                        alt={traveler?.fullName}
                        className="w-14 h-14 rounded-2xl object-cover border-2 border-emerald-500 shadow-sm flex-shrink-0"
                      />
                      <div>
                        <div className="flex items-center space-x-1.5">
                          <Link to={`/travelers/${traveler?.id}`} className="font-extrabold text-slate-900 text-lg leading-tight hover:text-emerald-700">
                            {traveler?.fullName}
                          </Link>
                          {traveler?.age && (
                            <span className="text-xs text-slate-400 font-medium">({traveler.age})</span>
                          )}
                        </div>
                        <p className="text-xs text-slate-500 flex items-center space-x-1 mt-0.5">
                          <MapPin className="w-3.5 h-3.5 text-emerald-600" />
                          <span>{traveler?.location || 'Global Nomad'}</span>
                        </p>
                      </div>
                    </div>

                    {/* Score Badge */}
                    <div className="text-right flex-shrink-0">
                      <div className={`px-3.5 py-1.5 rounded-2xl font-black text-base shadow-sm inline-flex items-center space-x-1 ${
                        score >= 80
                          ? 'bg-emerald-100 text-emerald-900 border border-emerald-200'
                          : score >= 60
                          ? 'bg-amber-100 text-amber-900 border border-amber-200'
                          : 'bg-slate-100 text-slate-800 border border-slate-200'
                      }`}>
                        <span>{score}%</span>
                      </div>
                      <p className="text-[10px] text-slate-400 font-bold uppercase tracking-wider mt-1">
                        {score >= 80 ? 'Excellent Match' : score >= 60 ? 'Strong Match' : 'Partial Match'}
                      </p>
                    </div>
                  </div>

                  {/* Trip Details & Itinerary note */}
                  <div className="bg-slate-50 p-4 rounded-2xl border border-slate-100 space-y-2 text-xs">
                    <div className="flex items-center justify-between text-slate-700 font-semibold">
                      <span className="flex items-center space-x-1 text-emerald-800">
                        <MapPin className="w-3.5 h-3.5 text-emerald-600" />
                        <span>{trip?.destination}</span>
                      </span>
                      <span className="text-slate-500">
                        {trip?.startDate} to {trip?.endDate}
                      </span>
                    </div>

                    <div className="flex items-center justify-between text-slate-600">
                      <span>Style: <strong className="text-slate-800">{trip?.travelStyle || 'SOLO'}</strong></span>
                      <span>Budget: <strong className="text-slate-800">₹{trip?.budget?.toLocaleString()}</strong></span>
                    </div>

                    {trip?.description && (
                      <p className="text-slate-500 italic pt-1 border-t border-slate-200/60 line-clamp-2">
                        "{trip.description}"
                      </p>
                    )}
                  </div>

                  {/* 5-Factor Score Breakdown Bar */}
                  <div className="space-y-1.5 text-[11px] text-slate-500">
                    <span className="font-bold text-slate-700">Algorithm Factor Scores:</span>
                    <div className="grid grid-cols-5 gap-1.5 text-center font-bold">
                      <div className="bg-emerald-50 text-emerald-800 p-1 rounded-lg">
                        Dest: {match.destinationScore}%
                      </div>
                      <div className="bg-teal-50 text-teal-800 p-1 rounded-lg">
                        Dates: {match.dateScore}%
                      </div>
                      <div className="bg-sky-50 text-sky-800 p-1 rounded-lg">
                        Interests: {match.interestScore}%
                      </div>
                      <div className="bg-amber-50 text-amber-800 p-1 rounded-lg">
                        Budget: {match.budgetScore}%
                      </div>
                      <div className="bg-purple-50 text-purple-800 p-1 rounded-lg">
                        Style: {match.styleScore}%
                      </div>
                    </div>
                  </div>

                  {/* Shared Interests Badges */}
                  {match.commonInterests && match.commonInterests.length > 0 && (
                    <div className="space-y-1">
                      <span className="text-[10px] uppercase font-bold text-slate-400 tracking-wider">
                        Common Interests ({match.commonInterests.length})
                      </span>
                      <div className="flex flex-wrap gap-1.5">
                        {match.commonInterests.map((interest, i) => (
                          <span
                            key={i}
                            className="text-[11px] font-semibold px-2 py-0.5 rounded-full bg-emerald-50 text-emerald-700 border border-emerald-100"
                          >
                            ✓ {interest}
                          </span>
                        ))}
                      </div>
                    </div>
                  )}

                  {/* Action Button */}
                  <div className="pt-2 border-t border-slate-100">
                    {match.connectionStatus === 'ACCEPTED' ? (
                      <div className="flex items-center justify-center space-x-1.5 py-2.5 rounded-xl bg-emerald-50 text-emerald-800 text-xs font-bold border border-emerald-200">
                        <CheckCircle2 className="w-4 h-4 text-emerald-600" />
                        <span>Connected Buddy</span>
                      </div>
                    ) : match.connectionStatus === 'PENDING' ? (
                      <div className="flex items-center justify-center space-x-1.5 py-2.5 rounded-xl bg-amber-50 text-amber-800 text-xs font-bold border border-amber-200">
                        <CheckCircle2 className="w-4 h-4 text-amber-600" />
                        <span>Connection Request Pending</span>
                      </div>
                    ) : (
                      <button
                        onClick={() => handleOpenConnectModal(match)}
                        className="w-full py-2.5 rounded-xl bg-emerald-700 hover:bg-emerald-800 text-white text-xs font-bold transition-colors shadow-sm flex items-center justify-center space-x-1.5"
                      >
                        <UserPlus className="w-4 h-4" />
                        <span>Connect with {traveler?.fullName?.split(' ')[0]}</span>
                      </button>
                    )}
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      )}

      {/* Connect Request Modal */}
      <Modal
        isOpen={connectModalOpen}
        onClose={() => setConnectModalOpen(false)}
        title={`Connect with ${selectedCandidate?.traveler?.fullName}`}
      >
        <form onSubmit={handleSendConnection} className="space-y-4">
          <div className="flex items-center space-x-3 p-3 bg-slate-50 rounded-xl">
            <img
              src={selectedCandidate?.traveler?.avatarUrl}
              alt={selectedCandidate?.traveler?.fullName}
              className="w-12 h-12 rounded-xl object-cover"
            />
            <div className="text-xs">
              <p className="font-bold text-slate-900">{selectedCandidate?.traveler?.fullName}</p>
              <p className="text-slate-500">{selectedCandidate?.trip?.destination} ({selectedCandidate?.compatibilityScore}% Match)</p>
            </div>
          </div>

          <div className="space-y-1">
            <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider">
              Introductory Message
            </label>
            <textarea
              value={connectMessage}
              onChange={(e) => setConnectMessage(e.target.value)}
              rows="4"
              required
              className="w-full p-3 rounded-xl border border-slate-200 focus:border-emerald-600 focus:ring-2 focus:ring-emerald-600/20 text-xs outline-none"
            />
          </div>

          <button
            type="submit"
            disabled={sendingRequest}
            className="w-full py-3 rounded-xl bg-emerald-700 hover:bg-emerald-800 text-white font-bold text-xs transition-colors shadow-md flex items-center justify-center space-x-1.5 disabled:opacity-50"
          >
            {sendingRequest ? (
              <span>Sending Request...</span>
            ) : (
              <>
                <Send className="w-3.5 h-3.5" />
                <span>Send Connection Request</span>
              </>
            )}
          </button>
        </form>
      </Modal>
    </div>
  );
};

export default BuddyMatches;
