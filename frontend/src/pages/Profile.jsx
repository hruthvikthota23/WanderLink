import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useToast } from '../context/ToastContext';
import api from '../services/api';
import { tripService } from '../services/tripService';
import { connectionService } from '../services/connectionService';
import LoadingSpinner from '../components/LoadingSpinner';
import { User, MapPin, Tag, Briefcase, Calendar, CheckCircle2, Save, Users, Sparkles } from 'lucide-react';

const Profile = () => {
  const { user, updateUser } = useAuth();
  const { showToast } = useToast();

  const [formData, setFormData] = useState({
    fullName: user?.fullName || '',
    location: user?.location || '',
    age: user?.age || '',
    bio: user?.bio || '',
    interests: user?.interests || '',
    travelStyle: user?.travelStyle || 'SOLO',
    avatarUrl: user?.avatarUrl || '',
  });

  const [saving, setSaving] = useState(false);
  const [trips, setTrips] = useState([]);
  const [connections, setConnections] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (user) {
      setFormData({
        fullName: user.fullName || '',
        location: user.location || '',
        age: user.age || '',
        bio: user.bio || '',
        interests: user.interests || '',
        travelStyle: user.travelStyle || 'SOLO',
        avatarUrl: user.avatarUrl || '',
      });
    }

    let active = true;
    let initialLoad = true;
    const loadProfileData = async () => {
      try {
        if (user?.id) {
          const [userTrips, userConn] = await Promise.all([
            tripService.getUserTrips(user.id).catch(() => []),
            connectionService.getUserConnections(user.id).catch(() => [])
          ]);
          if (active) {
            setTrips(userTrips);
            setConnections(userConn);
          }
        }
      } catch (err) {
        console.error('Error loading profile details:', err);
      } finally {
        if (active && initialLoad) setLoading(false);
        initialLoad = false;
      }
    };

    loadProfileData();
    const intervalId = window.setInterval(loadProfileData, 15000);
    return () => {
      active = false;
      window.clearInterval(intervalId);
    };
  }, [user]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleUpdate = async (e) => {
    e.preventDefault();
    try {
      setSaving(true);
      const res = await api.put(`/users/${user.id}`, {
        ...formData,
        age: formData.age ? parseInt(formData.age, 10) : null,
      });

      updateUser(res.data);
      showToast('Profile updated successfully!', 'success');
    } catch (err) {
      showToast(err.message || 'Failed to update profile', 'error');
    } finally {
      setSaving(false);
    }
  };

  const handleAcceptConnection = async (connId) => {
    try {
      await connectionService.acceptConnection(connId);
      showToast('Connection accepted!', 'success');
      setConnections((prev) =>
        prev.map((c) => (c.id === connId ? { ...c, status: 'ACCEPTED' } : c))
      );
    } catch (err) {
      showToast(err.message || 'Could not accept connection', 'error');
    }
  };

  const handleRejectConnection = async (connId) => {
    try {
      await connectionService.rejectConnection(connId);
      setConnections((prev) => prev.map((connection) =>
        connection.id === connId ? { ...connection, status: 'REJECTED' } : connection
      ));
      showToast('Connection request declined', 'success');
    } catch (err) {
      showToast(err.message || 'Could not decline connection', 'error');
    }
  };

  if (loading) {
    return <LoadingSpinner text="Loading profile details..." />;
  }

  return (
    <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      {/* Top Banner */}
      <div className="bg-gradient-to-r from-emerald-800 to-teal-700 rounded-3xl p-6 sm:p-8 text-white shadow-lg flex items-center space-x-6">
        <img
          src={formData.avatarUrl || "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&auto=format&fit=crop&q=80"}
          alt={formData.fullName}
          className="w-20 h-20 rounded-2xl object-cover border-2 border-white/40 shadow-md"
        />
        <div className="space-y-1">
          <span className="text-xs uppercase tracking-widest text-emerald-200 font-bold">
            {user?.role} • WanderLink Traveler
          </span>
          <h1 className="text-2xl sm:text-3xl font-extrabold tracking-tight">
            {formData.fullName}
          </h1>
          <p className="text-xs sm:text-sm text-emerald-100 flex items-center space-x-1">
            <MapPin className="w-3.5 h-3.5" />
            <span>{formData.location || 'Location Not Set'}</span>
          </p>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Left 2 Cols: Edit Form */}
        <div className="lg:col-span-2 bg-white rounded-3xl p-6 sm:p-8 border border-slate-100 shadow-sm space-y-6">
          <h2 className="text-lg font-bold text-slate-900 border-b border-slate-100 pb-3">
            Edit Profile Details
          </h2>

          <form onSubmit={handleUpdate} className="space-y-5">
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div className="space-y-1">
                <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider">
                  Full Name
                </label>
                <input
                  type="text"
                  name="fullName"
                  value={formData.fullName}
                  onChange={handleChange}
                  required
                  className="w-full p-2.5 rounded-xl border border-slate-200 text-xs outline-none focus:ring-2 focus:ring-emerald-600/20"
                />
              </div>

              <div className="space-y-1">
                <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider">
                  Home Location
                </label>
                <input
                  type="text"
                  name="location"
                  value={formData.location}
                  onChange={handleChange}
                  placeholder="e.g. Mumbai, India"
                  className="w-full p-2.5 rounded-xl border border-slate-200 text-xs outline-none focus:ring-2 focus:ring-emerald-600/20"
                />
              </div>

              <div className="space-y-1">
                <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider">
                  Age
                </label>
                <input
                  type="number"
                  name="age"
                  value={formData.age}
                  onChange={handleChange}
                  className="w-full p-2.5 rounded-xl border border-slate-200 text-xs outline-none focus:ring-2 focus:ring-emerald-600/20"
                />
              </div>

              <div className="space-y-1">
                <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider">
                  Travel Style
                </label>
                <select
                  name="travelStyle"
                  value={formData.travelStyle}
                  onChange={handleChange}
                  className="w-full p-2.5 rounded-xl border border-slate-200 text-xs outline-none bg-white font-medium"
                >
                  <option value="SOLO">Solo Explorer</option>
                  <option value="BACKPACKER">Backpacker</option>
                  <option value="BUDGET">Budget Conscious</option>
                  <option value="ADVENTURE">Adventure & Trekking</option>
                  <option value="CULTURAL">Cultural & Heritage</option>
                  <option value="LUXURY">Comfort & Leisure</option>
                </select>
              </div>
            </div>

            <div className="space-y-1">
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider">
                Avatar Image URL
              </label>
              <input
                type="url"
                name="avatarUrl"
                value={formData.avatarUrl}
                onChange={handleChange}
                placeholder="https://images.unsplash.com/..."
                className="w-full p-2.5 rounded-xl border border-slate-200 text-xs outline-none focus:ring-2 focus:ring-emerald-600/20"
              />
            </div>

            <div className="space-y-1">
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider">
                Interests (comma-separated)
              </label>
              <input
                type="text"
                name="interests"
                value={formData.interests}
                onChange={handleChange}
                placeholder="Beaches, Photography, Local Food, Trekking"
                className="w-full p-2.5 rounded-xl border border-slate-200 text-xs outline-none focus:ring-2 focus:ring-emerald-600/20"
              />
            </div>

            <div className="space-y-1">
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider">
                About You (Bio)
              </label>
              <textarea
                name="bio"
                value={formData.bio}
                onChange={handleChange}
                rows="4"
                className="w-full p-2.5 rounded-xl border border-slate-200 text-xs outline-none focus:ring-2 focus:ring-emerald-600/20"
              />
            </div>

            <button
              type="submit"
              disabled={saving}
              className="py-2.5 px-6 rounded-xl bg-emerald-700 hover:bg-emerald-800 text-white font-bold text-xs transition-colors shadow-sm flex items-center space-x-2 disabled:opacity-50"
            >
              <Save className="w-4 h-4" />
              <span>{saving ? 'Saving...' : 'Save Profile Changes'}</span>
            </button>
          </form>
        </div>

        {/* Right Col: Connections & Requests */}
        <div className="space-y-6">
          <div className="bg-white rounded-3xl p-6 border border-slate-100 shadow-sm space-y-4">
            <h3 className="text-sm font-bold text-slate-900 uppercase tracking-wider flex items-center justify-between border-b border-slate-100 pb-3">
              <span>Connection Requests</span>
              <span className="text-xs text-emerald-700 font-semibold">{connections.length}</span>
            </h3>

            {connections.length === 0 ? (
              <p className="text-xs text-slate-400 text-center py-4">
                No connection requests yet.
              </p>
            ) : (
              <div className="space-y-3">
                {connections.map((conn) => {
                  const isReceiver = conn.receiver?.id === user?.id;
                  const partner = isReceiver ? conn.sender : conn.receiver;
                  return (
                    <div key={conn.id} className="p-3 rounded-2xl bg-slate-50 border border-slate-100 space-y-2 text-xs">
                      <div className="flex items-center justify-between">
                        <div className="flex items-center space-x-2">
                          <img
                            src={partner?.avatarUrl || "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=100&auto=format&fit=crop&q=80"}
                            alt={partner?.fullName}
                            className="w-8 h-8 rounded-full object-cover"
                          />
                          <div>
                            <Link to={`/travelers/${partner?.id}`} className="font-bold text-slate-900 hover:text-emerald-700">{partner?.fullName}</Link>
                            <p className="text-[10px] text-slate-400">{partner?.location}</p>
                          </div>
                        </div>
                        <span className={`px-2 py-0.5 rounded-full text-[10px] font-bold ${
                          conn.status === 'ACCEPTED' ? 'bg-emerald-100 text-emerald-800' : 'bg-amber-100 text-amber-800'
                        }`}>
                          {conn.status}
                        </span>
                      </div>

                      {conn.message && (
                        <p className="text-slate-600 italic text-[11px] bg-white p-2 rounded-lg border border-slate-100">
                          "{conn.message}"
                        </p>
                      )}

                      {isReceiver && conn.status === 'PENDING' && (
                        <div className="pt-1 flex gap-2">
                          <button
                            onClick={() => handleAcceptConnection(conn.id)}
                            className="flex-1 py-1.5 rounded-lg bg-emerald-700 hover:bg-emerald-800 text-white font-bold text-[11px] transition-colors"
                          >
                            Accept
                          </button>
                          <button
                            type="button"
                            onClick={() => handleRejectConnection(conn.id)}
                            className="flex-1 py-1.5 rounded-lg bg-white border border-slate-200 text-slate-600 font-bold text-[11px] transition-colors"
                          >
                            Decline
                          </button>
                        </div>
                      )}
                    </div>
                  );
                })}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;
