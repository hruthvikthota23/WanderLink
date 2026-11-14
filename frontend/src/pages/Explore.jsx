import React, { useState, useEffect } from 'react';
import { useSearchParams, Link } from 'react-router-dom';
import { communityService } from '../services/communityService';
import { tripService } from '../services/tripService';
import LoadingSpinner from '../components/LoadingSpinner';
import { 
  Search, MapPin, Bed, Utensils, Flag, Users, Star, Calendar, IndianRupee,
  ChevronRight, Filter, Compass 
} from 'lucide-react';

const Explore = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const initialDestination = searchParams.get('destination') || '';

  const [activeTab, setActiveTab] = useState('all');
  const [searchQuery, setSearchQuery] = useState(initialDestination);
  const [accommodations, setAccommodations] = useState([]);
  const [foods, setFoods] = useState([]);
  const [guides, setGuides] = useState([]);
  const [groups, setGroups] = useState([]);
  const [trips, setTrips] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let active = true;
    let initialLoad = true;
    const fetchAllData = async () => {
      try {
        if (initialLoad) setLoading(true);
        const [accData, foodData, guideData, groupData, allTrips] = await Promise.all([
          communityService.getAccommodations(searchQuery).catch(() => []),
          communityService.getFoodListings(searchQuery).catch(() => []),
          communityService.getGuides(searchQuery).catch(() => []),
          communityService.getGroups(searchQuery).catch(() => []),
          tripService.getAllTrips().catch(() => [])
        ]);

        if (active) {
          setAccommodations(accData);
          setFoods(foodData);
          setGuides(guideData);
          setGroups(groupData);
          const query = searchQuery.trim().toLowerCase();
          setTrips(allTrips.filter((trip) => !query || trip.destination?.toLowerCase().includes(query)));
        }
      } catch (err) {
        console.error('Error fetching explore data:', err);
      } finally {
        if (active && initialLoad) setLoading(false);
        initialLoad = false;
      }
    };

    fetchAllData();
    const intervalId = window.setInterval(fetchAllData, 15000);
    return () => {
      active = false;
      window.clearInterval(intervalId);
    };
  }, [searchQuery]);

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    setSearchParams(searchQuery ? { destination: searchQuery } : {});
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      {/* Search Header Banner */}
      <div className="bg-gradient-to-r from-emerald-900 via-emerald-800 to-teal-900 rounded-3xl p-8 text-white shadow-xl space-y-6">
        <div className="max-w-2xl space-y-2">
          <span className="text-xs uppercase tracking-widest text-emerald-300 font-bold bg-white/10 px-3 py-1 rounded-full">
            Unified Community Discovery
          </span>
          <h1 className="text-3xl sm:text-4xl font-extrabold tracking-tight">
            Explore Homestays, Food & Guides
          </h1>
          <p className="text-xs sm:text-sm text-slate-200">
            Search authentic community-driven offerings across popular travel destinations.
          </p>
        </div>

        {/* Search bar */}
        <form onSubmit={handleSearchSubmit} className="max-w-xl flex items-center bg-white rounded-2xl p-1.5 shadow-md">
          <div className="flex items-center space-x-3 px-3 py-2 w-full text-slate-800">
            <Search className="w-5 h-5 text-emerald-600 flex-shrink-0" />
            <input
              type="text"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              placeholder="Search by destination (e.g. Goa, Manali, Jaipur)..."
              className="w-full bg-transparent text-sm focus:outline-none font-medium placeholder:text-slate-400"
            />
          </div>
          <button
            type="submit"
            className="px-5 py-2.5 rounded-xl bg-emerald-700 hover:bg-emerald-800 text-white font-semibold text-xs transition-colors flex-shrink-0"
          >
            Search
          </button>
        </form>

        {/* Tabs */}
        <div className="flex flex-wrap gap-2 pt-2 border-t border-white/10">
          {[
            { id: 'all', label: 'All Offerings', icon: Compass },
            { id: 'stays', label: `Homestays (${accommodations.length})`, icon: Bed },
            { id: 'food', label: `Local Food (${foods.length})`, icon: Utensils },
            { id: 'guides', label: `Guides (${guides.length})`, icon: Flag },
            { id: 'groups', label: `Groups (${groups.length})`, icon: Users },
            { id: 'trips', label: `Traveler Trips (${trips.length})`, icon: Calendar },
          ].map((tab) => {
            const Icon = tab.icon;
            const isSelected = activeTab === tab.id;
            return (
              <button
                key={tab.id}
                onClick={() => setActiveTab(tab.id)}
                className={`flex items-center space-x-1.5 px-4 py-2 rounded-xl text-xs font-bold transition-all ${
                  isSelected
                    ? 'bg-white text-emerald-900 shadow-md'
                    : 'bg-white/10 text-white hover:bg-white/20'
                }`}
              >
                <Icon className="w-3.5 h-3.5" />
                <span>{tab.label}</span>
              </button>
            );
          })}
        </div>
      </div>

      {loading ? (
        <LoadingSpinner text="Searching community offerings..." />
      ) : (
        <div className="space-y-12">
          {(activeTab === 'all' || activeTab === 'trips') && (
            <section className="space-y-4">
              <div className="flex items-center justify-between">
                <h2 className="text-xl font-bold text-slate-900 flex items-center space-x-2">
                  <Calendar className="w-5 h-5 text-emerald-700" />
                  <span>Trips Shared by Travelers</span>
                </h2>
                <span className="text-xs text-slate-500">Refreshes automatically</span>
              </div>
              {trips.length === 0 ? (
                <p className="text-xs text-slate-400 bg-white p-6 rounded-2xl border border-slate-100 text-center">
                  No traveler trips match this destination yet.
                </p>
              ) : (
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                  {trips.slice(0, 12).map((trip) => (
                    <article key={trip.id} className="bg-white rounded-2xl border border-slate-100 p-5 shadow-sm space-y-3">
                      <div className="flex items-center gap-2">
                        <img src={trip.userAvatar || 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=100&auto=format&fit=crop&q=80'} alt="" className="w-9 h-9 rounded-full object-cover" />
                        <Link to={`/travelers/${trip.userId}`} className="font-semibold text-sm text-slate-800 hover:text-emerald-700">{trip.userName || 'WanderLink traveler'}</Link>
                      </div>
                      <h3 className="font-extrabold text-slate-900 text-lg flex items-center gap-1"><MapPin className="w-4 h-4 text-emerald-700" />{trip.destination}</h3>
                      <p className="text-xs text-slate-500 flex items-center gap-1"><Calendar className="w-3.5 h-3.5" />{trip.startDate} to {trip.endDate}</p>
                      <p className="text-xs text-slate-600 flex items-center gap-1"><IndianRupee className="w-3.5 h-3.5" />{Number(trip.budget || 0).toLocaleString()} · {trip.travelStyle || 'Flexible'}</p>
                      {trip.description && <p className="text-xs text-slate-500 line-clamp-2">{trip.description}</p>}
                      <Link to={`/travelers/${trip.userId}`} className="block text-center py-2 rounded-xl bg-emerald-50 text-emerald-800 font-semibold text-xs hover:bg-emerald-100">View traveler and their trips</Link>
                    </article>
                  ))}
                </div>
              )}
            </section>
          )}

          {/* Accommodations Section */}
          {(activeTab === 'all' || activeTab === 'stays') && (
            <div className="space-y-4">
              <div className="flex items-center justify-between">
                <h2 className="text-xl font-bold text-slate-900 flex items-center space-x-2">
                  <Bed className="w-5 h-5 text-emerald-700" />
                  <span>Local Family Homestays</span>
                </h2>
                <Link to="/accommodations" className="text-xs font-semibold text-emerald-700 hover:text-emerald-800">
                  View All Stays →
                </Link>
              </div>

              {accommodations.length === 0 ? (
                <p className="text-xs text-slate-400 bg-white p-6 rounded-2xl border border-slate-100 text-center">
                  No accommodations match your search query.
                </p>
              ) : (
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                  {accommodations.map((stay) => (
                    <div key={stay.id} className="bg-white rounded-2xl border border-slate-100 overflow-hidden shadow-sm hover:shadow-lg transition-shadow">
                      <img src={stay.imageUrl} alt={stay.propertyName} className="h-44 w-full object-cover" />
                      <div className="p-5 space-y-2.5">
                        <div className="flex items-center justify-between">
                          <span className="text-xs font-semibold text-emerald-700 bg-emerald-50 px-2 py-0.5 rounded-md">
                            {stay.location}
                          </span>
                          <span className="font-extrabold text-slate-900">₹{stay.pricePerNight}/night</span>
                        </div>
                        <h3 className="font-bold text-slate-900 text-base">{stay.propertyName}</h3>
                        <p className="text-xs text-slate-500 line-clamp-2">{stay.description}</p>
                        <p className="text-[11px] text-slate-400 truncate">{stay.amenities}</p>
                        <Link
                          to="/accommodations"
                          className="block text-center w-full py-2 rounded-xl border border-emerald-700 text-emerald-800 font-semibold text-xs hover:bg-emerald-50 transition-colors mt-2"
                        >
                          Contact Host
                        </Link>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>
          )}

          {/* Food Listings Section */}
          {(activeTab === 'all' || activeTab === 'food') && (
            <div className="space-y-4">
              <div className="flex items-center justify-between">
                <h2 className="text-xl font-bold text-slate-900 flex items-center space-x-2">
                  <Utensils className="w-5 h-5 text-orange-600" />
                  <span>Homemade Food Experiences</span>
                </h2>
                <Link to="/food" className="text-xs font-semibold text-orange-700 hover:text-orange-800">
                  View All Food →
                </Link>
              </div>

              {foods.length === 0 ? (
                <p className="text-xs text-slate-400 bg-white p-6 rounded-2xl border border-slate-100 text-center">
                  No food listings match your search query.
                </p>
              ) : (
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                  {foods.map((food) => (
                    <div key={food.id} className="bg-white rounded-2xl border border-slate-100 overflow-hidden shadow-sm hover:shadow-lg transition-shadow">
                      <img src={food.imageUrl} alt={food.foodName} className="h-44 w-full object-cover" />
                      <div className="p-5 space-y-2.5">
                        <div className="flex items-center justify-between">
                          <span className="text-xs font-semibold text-orange-700 bg-orange-50 px-2 py-0.5 rounded-md">
                            {food.cuisine}
                          </span>
                          <span className="font-extrabold text-slate-900">₹{food.price}</span>
                        </div>
                        <h3 className="font-bold text-slate-900 text-base">{food.foodName}</h3>
                        <p className="text-xs text-slate-500 line-clamp-2">{food.description}</p>
                        <p className="text-[11px] text-slate-400 flex items-center space-x-1">
                          <MapPin className="w-3 h-3 text-orange-500" />
                          <span>{food.location}</span>
                        </p>
                        <Link
                          to="/food"
                          className="block text-center w-full py-2 rounded-xl bg-orange-50 text-orange-800 font-semibold text-xs hover:bg-orange-100 transition-colors mt-2"
                        >
                          Reserve Experience
                        </Link>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>
          )}

          {/* Local Guides Section */}
          {(activeTab === 'all' || activeTab === 'guides') && (
            <div className="space-y-4">
              <div className="flex items-center justify-between">
                <h2 className="text-xl font-bold text-slate-900 flex items-center space-x-2">
                  <Flag className="w-5 h-5 text-teal-700" />
                  <span>Certified Local Guides</span>
                </h2>
                <Link to="/guides" className="text-xs font-semibold text-teal-700 hover:text-teal-800">
                  View All Guides →
                </Link>
              </div>

              {guides.length === 0 ? (
                <p className="text-xs text-slate-400 bg-white p-6 rounded-2xl border border-slate-100 text-center">
                  No local guides found for this search.
                </p>
              ) : (
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                  {guides.map((guide) => (
                    <div key={guide.id} className="bg-white rounded-2xl border border-slate-100 p-5 shadow-sm hover:shadow-lg transition-all flex space-x-4">
                      <img src={guide.imageUrl} alt={guide.guideName} className="w-20 h-20 rounded-2xl object-cover flex-shrink-0" />
                      <div className="space-y-1 text-xs">
                        <div className="flex items-center space-x-1 text-amber-500 font-bold">
                          <Star className="w-3.5 h-3.5 fill-current" />
                          <span>{guide.rating || 5.0}</span>
                        </div>
                        <h3 className="font-bold text-slate-900 text-base">{guide.guideName}</h3>
                        <p className="text-slate-500">Destination: <strong className="text-slate-700">{guide.destination}</strong></p>
                        <p className="text-slate-400 line-clamp-1">{guide.languages}</p>
                        <p className="font-bold text-emerald-800 text-sm pt-1">₹{guide.pricePerDay}/day</p>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>
          )}

          {/* Travel Groups Section */}
          {(activeTab === 'all' || activeTab === 'groups') && (
            <div className="space-y-4">
              <div className="flex items-center justify-between">
                <h2 className="text-xl font-bold text-slate-900 flex items-center space-x-2">
                  <Users className="w-5 h-5 text-purple-700" />
                  <span>Travel Groups & Clubs</span>
                </h2>
                <Link to="/groups" className="text-xs font-semibold text-purple-700 hover:text-purple-800">
                  View All Groups →
                </Link>
              </div>

              {groups.length === 0 ? (
                <p className="text-xs text-slate-400 bg-white p-6 rounded-2xl border border-slate-100 text-center">
                  No travel groups match your query.
                </p>
              ) : (
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                  {groups.map((group) => (
                    <div key={group.id} className="bg-white rounded-2xl border border-slate-100 overflow-hidden shadow-sm hover:shadow-lg transition-shadow">
                      <img src={group.imageUrl} alt={group.name} className="h-40 w-full object-cover" />
                      <div className="p-5 space-y-2">
                        <span className="text-[10px] font-bold uppercase tracking-wider text-purple-700 bg-purple-50 px-2 py-0.5 rounded-full">
                          {group.destination} • {group.memberCount} members
                        </span>
                        <h3 className="font-bold text-slate-900 text-base">{group.name}</h3>
                        <p className="text-xs text-slate-500 line-clamp-2">{group.description}</p>
                        <Link
                          to="/groups"
                          className="block text-center w-full py-2 rounded-xl bg-slate-900 text-white font-semibold text-xs hover:bg-slate-800 transition-colors mt-3"
                        >
                          View Community
                        </Link>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default Explore;
