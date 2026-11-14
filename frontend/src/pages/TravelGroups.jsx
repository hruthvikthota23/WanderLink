import React, { useState, useEffect } from 'react';
import { communityService } from '../services/communityService';
import { useAuth } from '../context/AuthContext';
import { useToast } from '../context/ToastContext';
import LoadingSpinner from '../components/LoadingSpinner';
import Modal from '../components/Modal';
import { Users, MapPin, PlusCircle, CheckCircle2, UserPlus, LogOut, Search } from 'lucide-react';

const TravelGroups = () => {
  const [groups, setGroups] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchDest, setSearchDest] = useState('');

  // Create Group Modal
  const [createModalOpen, setCreateModalOpen] = useState(false);
  const [newGroup, setNewGroup] = useState({
    name: '',
    destination: 'Goa',
    description: '',
    imageUrl: '',
  });
  const [creating, setCreating] = useState(false);

  const { user, isAuthenticated } = useAuth();
  const { showToast } = useToast();

  const fetchGroups = async () => {
    try {
      setLoading(true);
      const data = await communityService.getGroups(searchDest);
      setGroups(data);
    } catch (err) {
      console.error('Error fetching travel groups:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchGroups();
  }, [searchDest]);

  const handleToggleJoin = async (group) => {
    if (!isAuthenticated) {
      showToast('Please sign in to join travel groups', 'info');
      return;
    }

    try {
      if (group.isMember) {
        await communityService.leaveGroup(group.id);
        showToast(`Left ${group.name}`, 'info');
        setGroups((prev) =>
          prev.map((g) =>
            g.id === group.id
              ? { ...g, isMember: false, memberCount: Math.max(0, g.memberCount - 1) }
              : g
          )
        );
      } else {
        await communityService.joinGroup(group.id);
        showToast(`Joined ${group.name}! Welcome to the group.`, 'success');
        setGroups((prev) =>
          prev.map((g) =>
            g.id === group.id
              ? { ...g, isMember: true, memberCount: g.memberCount + 1 }
              : g
          )
        );
      }
    } catch (err) {
      showToast(err.message || 'Action failed', 'error');
    }
  };

  const handleCreateGroup = async (e) => {
    e.preventDefault();
    if (!isAuthenticated) {
      showToast('Please sign in to create a travel group', 'info');
      return;
    }

    try {
      setCreating(true);
      const created = await communityService.createGroup(newGroup);
      showToast(`Group "${created.name}" created successfully!`, 'success');
      setGroups((prev) => [created, ...prev]);
      setCreateModalOpen(false);
      setNewGroup({ name: '', destination: 'Goa', description: '', imageUrl: '' });
    } catch (err) {
      showToast(err.message || 'Failed to create group', 'error');
    } finally {
      setCreating(false);
    }
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      {/* Banner */}
      <div className="bg-gradient-to-r from-purple-900 via-indigo-900 to-slate-900 rounded-3xl p-8 text-white shadow-lg flex flex-col md:flex-row md:items-center justify-between gap-6">
        <div className="space-y-3 max-w-2xl">
          <div className="inline-flex items-center space-x-2 text-xs font-bold uppercase tracking-wider bg-white/10 px-3 py-1 rounded-full">
            <Users className="w-3.5 h-3.5" />
            <span>Community Circles & Clubs</span>
          </div>
          <h1 className="text-3xl sm:text-4xl font-extrabold tracking-tight">
            Destination Travel Groups
          </h1>
          <p className="text-xs sm:text-sm text-purple-200 leading-relaxed">
            Connect with active circles of solo travelers, backpackers, and nomads visiting the same destinations. Plan sunset volleyball, group treks, and cafe work sessions.
          </p>

          <div className="pt-2 max-w-sm">
            <div className="flex items-center space-x-2 bg-white text-slate-800 px-3.5 py-2 rounded-xl border border-white/20 shadow-sm text-xs">
              <Search className="w-4 h-4 text-purple-600 flex-shrink-0" />
              <input
                type="text"
                value={searchDest}
                onChange={(e) => setSearchDest(e.target.value)}
                placeholder="Filter groups by destination (e.g. Goa, Manali)..."
                className="w-full bg-transparent focus:outline-none font-medium"
              />
            </div>
          </div>
        </div>

        <button
          onClick={() => setCreateModalOpen(true)}
          className="inline-flex items-center space-x-2 px-5 py-3 rounded-xl bg-orange-600 hover:bg-orange-700 text-white font-bold text-xs shadow-md transition-colors flex-shrink-0 self-start md:self-center"
        >
          <PlusCircle className="w-4 h-4" />
          <span>Create Travel Group</span>
        </button>
      </div>

      {loading ? (
        <LoadingSpinner text="Loading travel groups..." />
      ) : groups.length === 0 ? (
        <div className="p-12 text-center bg-white rounded-3xl border border-slate-100 space-y-3">
          <Users className="w-10 h-10 text-slate-300 mx-auto" />
          <h3 className="text-base font-bold text-slate-800">No travel groups found</h3>
          <p className="text-xs text-slate-500">Be the first to start a group for this destination!</p>
          <button
            onClick={() => setCreateModalOpen(true)}
            className="px-4 py-2 rounded-xl bg-purple-700 text-white text-xs font-semibold"
          >
            Create Group
          </button>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {groups.map((group) => (
            <div
              key={group.id}
              className="bg-white rounded-3xl border border-slate-100 overflow-hidden shadow-sm hover:shadow-xl transition-all flex flex-col justify-between"
            >
              <div>
                <div className="relative h-44 overflow-hidden">
                  <img
                    src={group.imageUrl || "https://images.unsplash.com/photo-1512343879784-a960bf40e7f2?w=600&auto=format&fit=crop&q=80"}
                    alt={group.name}
                    className="w-full h-full object-cover hover:scale-105 transition-transform duration-300"
                  />
                  <div className="absolute top-3 left-3 bg-white/95 backdrop-blur-md px-2.5 py-1 rounded-full text-[11px] font-extrabold text-purple-900 shadow-sm flex items-center space-x-1">
                    <MapPin className="w-3 h-3 text-purple-600" />
                    <span>{group.destination}</span>
                  </div>
                  <div className="absolute top-3 right-3 bg-slate-900/80 backdrop-blur-md px-2.5 py-1 rounded-full text-[11px] font-semibold text-white shadow-sm flex items-center space-x-1">
                    <Users className="w-3 h-3 text-emerald-400" />
                    <span>{group.memberCount} members</span>
                  </div>
                </div>

                <div className="p-5 space-y-2.5">
                  <h3 className="font-extrabold text-slate-900 text-lg leading-snug">
                    {group.name}
                  </h3>
                  <p className="text-xs text-slate-600 leading-relaxed line-clamp-3">
                    {group.description}
                  </p>
                  {group.creatorName && (
                    <p className="text-[11px] text-slate-400 pt-1">
                      Created by <strong className="text-slate-600">{group.creatorName}</strong>
                    </p>
                  )}
                </div>
              </div>

              <div className="p-5 pt-0">
                <button
                  onClick={() => handleToggleJoin(group)}
                  className={`w-full py-2.5 rounded-xl text-xs font-bold transition-colors flex items-center justify-center space-x-1.5 shadow-sm ${
                    group.isMember
                      ? 'bg-slate-100 hover:bg-red-50 hover:text-red-700 text-slate-700 border border-slate-200'
                      : 'bg-purple-700 hover:bg-purple-800 text-white'
                  }`}
                >
                  {group.isMember ? (
                    <>
                      <CheckCircle2 className="w-4 h-4 text-emerald-600" />
                      <span>Joined Member (Click to Leave)</span>
                    </>
                  ) : (
                    <>
                      <UserPlus className="w-4 h-4" />
                      <span>Join Community Group</span>
                    </>
                  )}
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Create Group Modal */}
      <Modal
        isOpen={createModalOpen}
        onClose={() => setCreateModalOpen(false)}
        title="Start a New Travel Community"
      >
        <form onSubmit={handleCreateGroup} className="space-y-4">
          <div className="space-y-1">
            <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider">
              Group Name *
            </label>
            <input
              type="text"
              value={newGroup.name}
              onChange={(e) => setNewGroup({ ...newGroup, name: e.target.value })}
              placeholder="e.g. Goa Sunset Surfers Club"
              required
              className="w-full p-2.5 rounded-xl border border-slate-200 text-xs outline-none"
            />
          </div>

          <div className="space-y-1">
            <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider">
              Destination *
            </label>
            <input
              type="text"
              value={newGroup.destination}
              onChange={(e) => setNewGroup({ ...newGroup, destination: e.target.value })}
              placeholder="e.g. Goa, Manali, Kerala"
              required
              className="w-full p-2.5 rounded-xl border border-slate-200 text-xs outline-none"
            />
          </div>

          <div className="space-y-1">
            <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider">
              Description *
            </label>
            <textarea
              value={newGroup.description}
              onChange={(e) => setNewGroup({ ...newGroup, description: e.target.value })}
              rows="3"
              placeholder="What activities will this group do? Who should join?"
              required
              className="w-full p-2.5 rounded-xl border border-slate-200 text-xs outline-none"
            />
          </div>

          <button
            type="submit"
            disabled={creating}
            className="w-full py-3 rounded-xl bg-purple-700 hover:bg-purple-800 text-white font-bold text-xs transition-colors shadow-md disabled:opacity-50"
          >
            {creating ? 'Creating Group...' : 'Create & Join Group'}
          </button>
        </form>
      </Modal>
    </div>
  );
};

export default TravelGroups;
