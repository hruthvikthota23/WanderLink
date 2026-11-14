import React, { useCallback, useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Calendar, MapPin, IndianRupee, Compass, Users } from 'lucide-react';
import api from '../services/api';
import { tripService } from '../services/tripService';
import LoadingSpinner from '../components/LoadingSpinner';

const TravelerProfile = () => {
  const { userId } = useParams();
  const [traveler, setTraveler] = useState(null);
  const [trips, setTrips] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const refreshProfile = useCallback(async () => {
    try {
      const [profileResponse, userTrips] = await Promise.all([
        api.get(`/users/${userId}`),
        tripService.getUserTrips(userId),
      ]);
      setTraveler(profileResponse.data);
      setTrips(userTrips);
      setError('');
    } catch (err) {
      setError(err.message || 'Could not load this traveler profile');
    } finally {
      setLoading(false);
    }
  }, [userId]);

  useEffect(() => {
    setLoading(true);
    refreshProfile();
    const intervalId = window.setInterval(refreshProfile, 15000);
    return () => window.clearInterval(intervalId);
  }, [refreshProfile]);

  if (loading) return <LoadingSpinner text="Loading traveler profile..." />;

  if (error || !traveler) {
    return (
      <div className="max-w-3xl mx-auto px-4 py-16 text-center">
        <p className="text-slate-600">{error || 'Traveler not found'}</p>
        <Link to="/matches" className="inline-block mt-4 text-emerald-700 font-semibold">Back to travel buddies</Link>
      </div>
    );
  }

  return (
    <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      <section className="bg-gradient-to-r from-emerald-800 to-teal-700 rounded-3xl p-6 sm:p-8 text-white shadow-lg flex items-center gap-5">
        <img
          src={traveler.avatarUrl || 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150&auto=format&fit=crop&q=80'}
          alt={traveler.fullName}
          className="w-20 h-20 rounded-2xl object-cover border-2 border-white/40"
        />
        <div>
          <p className="text-xs uppercase tracking-widest text-emerald-200 font-bold">{traveler.role || 'Traveler'} on WanderLink</p>
          <h1 className="text-2xl sm:text-3xl font-extrabold">{traveler.fullName}</h1>
          <p className="mt-1 text-sm text-emerald-100 flex items-center gap-1"><MapPin className="w-4 h-4" />{traveler.location || 'Location not set'}</p>
        </div>
      </section>

      <section className="bg-white rounded-3xl p-6 border border-slate-100 shadow-sm space-y-3">
        <div className="flex items-center gap-2 text-slate-800 font-bold"><Compass className="w-4 h-4 text-emerald-700" />About {traveler.fullName?.split(' ')[0]}</div>
        <p className="text-sm text-slate-600">{traveler.bio || 'This traveler has not added a bio yet.'}</p>
        <div className="flex flex-wrap gap-2 text-xs">
          {traveler.travelStyle && <span className="px-3 py-1 rounded-full bg-emerald-50 text-emerald-800">{traveler.travelStyle}</span>}
          {traveler.age && <span className="px-3 py-1 rounded-full bg-slate-100 text-slate-700">{traveler.age} years old</span>}
          {traveler.interests?.split(',').map((interest) => interest.trim()).filter(Boolean).map((interest) => (
            <span key={interest} className="px-3 py-1 rounded-full bg-slate-100 text-slate-700">{interest}</span>
          ))}
        </div>
      </section>

      <section className="space-y-4">
        <div className="flex items-center justify-between">
          <h2 className="text-xl font-extrabold text-slate-900 flex items-center gap-2"><Users className="w-5 h-5 text-emerald-700" />Published Trips</h2>
          <span className="text-xs font-semibold text-slate-500">Updates automatically</span>
        </div>
        {trips.length === 0 ? (
          <div className="bg-white rounded-2xl p-8 text-center border border-slate-100 text-sm text-slate-500">No trips published yet.</div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {trips.map((trip) => (
              <article key={trip.id} className="bg-white rounded-2xl p-5 border border-slate-100 shadow-sm space-y-3">
                <div className="flex items-start justify-between gap-3">
                  <h3 className="font-bold text-slate-900 text-lg flex items-center gap-2"><MapPin className="w-4 h-4 text-emerald-700" />{trip.destination}</h3>
                  {trip.travelStyle && <span className="text-[10px] font-bold uppercase px-2 py-1 rounded-full bg-emerald-50 text-emerald-800">{trip.travelStyle}</span>}
                </div>
                <p className="text-xs text-slate-500 flex items-center gap-1"><Calendar className="w-3.5 h-3.5" />{trip.startDate} to {trip.endDate}</p>
                {trip.budget != null && <p className="text-xs text-slate-600 flex items-center gap-1"><IndianRupee className="w-3.5 h-3.5" />Budget {Number(trip.budget).toLocaleString()}</p>}
                {trip.description && <p className="text-sm text-slate-600">{trip.description}</p>}
                {trip.interests && <p className="text-xs text-emerald-800">Interests: {trip.interests}</p>}
              </article>
            ))}
          </div>
        )}
      </section>
    </div>
  );
};

export default TravelerProfile;
