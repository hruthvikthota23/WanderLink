import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { tripService } from '../services/tripService';
import { connectionService } from '../services/connectionService';
import { communityService } from '../services/communityService';
import LoadingSpinner from '../components/LoadingSpinner';
import { 
  PlusCircle, Users, Compass, Calendar, MapPin, Sparkles, 
  ArrowRight, Heart, Bed, Utensils, MessageSquare, CheckCircle2 
} from 'lucide-react';

const Dashboard = () => {
  const { user } = useAuth();
  const [trips, setTrips] = useState([]);
  const [connections, setConnections] = useState([]);
  const [groups, setGroups] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let active = true;
    let initialLoad = true;
    const fetchDashboardData = async () => {
      try {
        if (user?.id) {
          const [userTrips, userConn, allGroups] = await Promise.all([
            tripService.getUserTrips(user.id).catch(() => []),
            connectionService.getUserConnections(user.id).catch(() => []),
            communityService.getGroups().catch(() => [])
          ]);
          if (active) {
            setTrips(userTrips);
            setConnections(userConn);
            setGroups(allGroups.slice(0, 3));
          }
        }
      } catch (err) {
        console.error('Error fetching dashboard data:', err);
      } finally {
        if (active && initialLoad) setLoading(false);
        initialLoad = false;
      }
    };

    fetchDashboardData();
    const intervalId = window.setInterval(fetchDashboardData, 15000);
    return () => {
      active = false;
      window.clearInterval(intervalId);
    };
  }, [user]);

  if (loading) {
    return <LoadingSpinner text="Loading traveler dashboard..." />;
  }

  const acceptedConnections = connections.filter((c) => c.status === 'ACCEPTED');
  const pendingConnections = connections.filter((c) => c.status === 'PENDING');

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-8">
      {/* Welcome & Profile Summary Banner */}
      <div className="bg-gradient-to-r from-emerald-800 to-teal-700 rounded-3xl p-6 sm:p-8 text-white shadow-lg flex flex-col md:flex-row items-start md:items-center justify-between gap-6">
        <div className="flex items-center space-x-4">
          <img
            src={user?.avatarUrl || "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&auto=format&fit=crop&q=80"}
            alt={user?.fullName}
            className="w-16 h-16 sm:w-20 sm:h-20 rounded-2xl object-cover border-2 border-white/30 shadow-md"
          />
          <div className="space-y-1">
            <span className="text-xs uppercase tracking-widest text-emerald-200 font-bold">
              {user?.role || 'TRAVELER'} • {user?.travelStyle || 'SOLO'}
            </span>
            <h1 className="text-2xl sm:text-3xl font-extrabold tracking-tight">
              Welcome back, {user?.fullName}!
            </h1>
            <p className="text-xs sm:text-sm text-emerald-100 flex items-center space-x-1">
              <MapPin className="w-3.5 h-3.5" />
              <span>Based in {user?.location || 'Somewhere Wonderful'}</span>
            </p>
          </div>
        </div>

        {/* Quick Stats Pill */}
        <div className="flex items-center space-x-4 bg-white/10 backdrop-blur-md px-6 py-3.5 rounded-2xl border border-white/20 self-stretch sm:self-auto justify-around">
          <div className="text-center">
            <span className="block text-xl font-black">{trips.length}</span>
            <span className="text-[11px] text-emerald-200 uppercase font-semibold">Trips</span>
          </div>
          <div className="w-px h-8 bg-white/20" />
          <div className="text-center">
            <span className="block text-xl font-black">{acceptedConnections.length}</span>
            <span className="text-[11px] text-emerald-200 uppercase font-semibold">Buddies</span>
          </div>
          <div className="w-px h-8 bg-white/20" />
          <div className="text-center">
            <span className="block text-xl font-black text-amber-300">{pendingConnections.length}</span>
            <span className="text-[11px] text-emerald-200 uppercase font-semibold">Pending</span>
          </div>
        </div>
      </div>

      {/* Quick Action Shortcuts */}
      <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
        <Link
          to="/create-trip"
          className="p-4 rounded-2xl bg-white border border-slate-100 shadow-sm hover:shadow-md transition-shadow flex items-center space-x-3 group"
        >
          <div className="w-10 h-10 rounded-xl bg-emerald-50 text-emerald-700 flex items-center justify-center group-hover:bg-emerald-700 group-hover:text-white transition-colors">
            <PlusCircle className="w-5 h-5" />
          </div>
          <div>
            <span className="block text-sm font-bold text-slate-900">Post Trip</span>
            <span className="text-xs text-slate-500">Plan new journey</span>
          </div>
        </Link>

        <Link
          to="/matches"
          className="p-4 rounded-2xl bg-white border border-slate-100 shadow-sm hover:shadow-md transition-shadow flex items-center space-x-3 group"
        >
          <div className="w-10 h-10 rounded-xl bg-teal-50 text-teal-700 flex items-center justify-center group-hover:bg-teal-700 group-hover:text-white transition-colors">
            <Users className="w-5 h-5" />
          </div>
          <div>
            <span className="block text-sm font-bold text-slate-900">Find Buddies</span>
            <span className="text-xs text-slate-500">Calculate matches</span>
          </div>
        </Link>

        <Link
          to="/ai-assistant"
          className="p-4 rounded-2xl bg-white border border-slate-100 shadow-sm hover:shadow-md transition-shadow flex items-center space-x-3 group"
        >
          <div className="w-10 h-10 rounded-xl bg-orange-50 text-orange-600 flex items-center justify-center group-hover:bg-orange-600 group-hover:text-white transition-colors">
            <Sparkles className="w-5 h-5" />
          </div>
          <div>
            <span className="block text-sm font-bold text-slate-900">AI Assistant</span>
            <span className="text-xs text-slate-500">Curate itinerary</span>
          </div>
        </Link>

        <Link
          to="/explore"
          className="p-4 rounded-2xl bg-white border border-slate-100 shadow-sm hover:shadow-md transition-shadow flex items-center space-x-3 group"
        >
          <div className="w-10 h-10 rounded-xl bg-purple-50 text-purple-700 flex items-center justify-center group-hover:bg-purple-700 group-hover:text-white transition-colors">
            <Compass className="w-5 h-5" />
          </div>
          <div>
            <span className="block text-sm font-bold text-slate-900">Explore Local</span>
            <span className="text-xs text-slate-500">Stays & cuisines</span>
          </div>
        </Link>
      </div>

      {/* Main Grid: Upcoming Trips & Connections */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Left 2 Cols: My Trips */}
        <div className="lg:col-span-2 space-y-4">
          <div className="flex items-center justify-between">
            <h2 className="text-lg font-bold text-slate-900 flex items-center space-x-2">
              <Calendar className="w-5 h-5 text-emerald-700" />
              <span>My Planned Trips</span>
            </h2>
            <Link to="/create-trip" className="text-xs font-semibold text-emerald-700 hover:text-emerald-800">
              + New Trip
            </Link>
          </div>

          {trips.length === 0 ? (
            <div className="p-8 text-center bg-white rounded-2xl border border-slate-100 space-y-3">
              <Compass className="w-8 h-8 text-slate-300 mx-auto" />
              <p className="text-sm font-semibold text-slate-700">No active trips posted yet</p>
              <p className="text-xs text-slate-400 max-w-sm mx-auto">
                Create your first trip (e.g. to Goa) so our smart matching algorithm can recommend compatible buddies!
              </p>
              <Link
                to="/create-trip"
                className="inline-flex items-center space-x-2 px-4 py-2 rounded-xl bg-emerald-700 text-white text-xs font-semibold shadow-sm hover:bg-emerald-800"
              >
                <span>Create a Goa Trip</span>
              </Link>
            </div>
          ) : (
            <div className="space-y-4">
              {trips.map((trip) => (
                <div
                  key={trip.id}
                  className="bg-white rounded-2xl p-5 border border-slate-100 shadow-sm hover:shadow-md transition-shadow space-y-3"
                >
                  <div className="flex items-start justify-between">
                    <div>
                      <div className="flex items-center space-x-2">
                        <span className="text-base font-bold text-slate-900">{trip.destination}</span>
                        <span className="text-xs font-semibold px-2 py-0.5 rounded-full bg-emerald-50 text-emerald-700">
                          {trip.travelStyle || 'SOLO'}
                        </span>
                      </div>
                      <p className="text-xs text-slate-500 mt-0.5">
                        {trip.startDate} to {trip.endDate}
                      </p>
                    </div>
                    <span className="font-extrabold text-slate-900 text-sm">
                      ₹{trip.budget?.toLocaleString()}
                    </span>
                  </div>

                  {trip.description && (
                    <p className="text-xs text-slate-600 line-clamp-2 italic">
                      "{trip.description}"
                    </p>
                  )}

                  <div className="pt-2 border-t border-slate-100 flex items-center justify-between">
                    <span className="text-[11px] text-slate-400">
                      Interests: {trip.interests || 'Beaches, Exploring'}
                    </span>
                    <Link
                      to={`/matches?tripId=${trip.id}`}
                      className="inline-flex items-center space-x-1.5 px-3 py-1.5 rounded-lg bg-emerald-700 hover:bg-emerald-800 text-white text-xs font-semibold transition-colors"
                    >
                      <Users className="w-3.5 h-3.5" />
                      <span>Find Buddies</span>
                    </Link>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Right Col: Connection Requests & Communities */}
        <div className="space-y-6">
          {/* Connections */}
          <div className="bg-white rounded-2xl p-5 border border-slate-100 shadow-sm space-y-4">
            <h3 className="text-sm font-bold text-slate-900 uppercase tracking-wider flex items-center justify-between">
              <span>Travel Connections</span>
              <span className="text-xs text-emerald-700 font-semibold">{connections.length} Total</span>
            </h3>

            {connections.length === 0 ? (
              <p className="text-xs text-slate-400 text-center py-4">
                No active connections yet. Head to "Find Travel Buddies" to start connecting!
              </p>
            ) : (
              <div className="space-y-3">
                {connections.slice(0, 4).map((conn) => {
                  const other = conn.sender?.id === user?.id ? conn.receiver : conn.sender;
                  return (
                    <div key={conn.id} className="flex items-center justify-between text-xs p-2 rounded-xl bg-slate-50">
                      <div className="flex items-center space-x-2.5">
                        <img
                          src={other?.avatarUrl || "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=100&auto=format&fit=crop&q=80"}
                          alt={other?.fullName}
                          className="w-8 h-8 rounded-full object-cover"
                        />
                        <div>
                          <Link to={`/travelers/${other?.id}`} className="font-bold text-slate-900 hover:text-emerald-700">{other?.fullName}</Link>
                          <p className="text-[10px] text-slate-400">{other?.location}</p>
                        </div>
                      </div>
                      <span className={`px-2 py-0.5 rounded-full text-[10px] font-bold ${
                        conn.status === 'ACCEPTED'
                          ? 'bg-emerald-100 text-emerald-800'
                          : conn.status === 'PENDING'
                          ? 'bg-amber-100 text-amber-800'
                          : 'bg-red-100 text-red-800'
                      }`}>
                        {conn.status}
                      </span>
                    </div>
                  );
                })}
              </div>
            )}
          </div>

          {/* Communities teaser */}
          <div className="bg-white rounded-2xl p-5 border border-slate-100 shadow-sm space-y-3">
            <h3 className="text-sm font-bold text-slate-900 uppercase tracking-wider flex items-center justify-between">
              <span>Featured Communities</span>
              <Link to="/groups" className="text-xs text-purple-700 font-semibold">View All</Link>
            </h3>

            <div className="space-y-2.5">
              {groups.map((group) => (
                <Link
                  key={group.id}
                  to="/groups"
                  className="flex items-center space-x-3 p-2 rounded-xl hover:bg-slate-50 transition-colors"
                >
                  <img
                    src={group.imageUrl}
                    alt={group.name}
                    className="w-10 h-10 rounded-xl object-cover flex-shrink-0"
                  />
                  <div className="text-xs">
                    <p className="font-bold text-slate-900 leading-snug">{group.name}</p>
                    <p className="text-[11px] text-slate-400">{group.destination} • {group.memberCount} members</p>
                  </div>
                </Link>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
