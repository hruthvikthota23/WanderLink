import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { tripService } from '../services/tripService';
import { useToast } from '../context/ToastContext';
import { MapPin, Calendar, IndianRupee, Tag, Compass, ArrowRight, Loader2 } from 'lucide-react';

const CreateTrip = () => {
  const [formData, setFormData] = useState({
    destination: 'Goa',
    startDate: '',
    endDate: '',
    budget: '12000',
    interests: 'Beaches, Photography, Local Food, Sunsets',
    travelStyle: 'SOLO',
    description: '',
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const navigate = useNavigate();
  const { showToast } = useToast();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleInterestToggle = (interest) => {
    setFormData((prev) => {
      const current = prev.interests ? prev.interests.split(',').map((s) => s.trim()) : [];
      if (current.includes(interest)) {
        return { ...prev, interests: current.filter((i) => i !== interest).join(', ') };
      } else {
        return { ...prev, interests: [...current, interest].join(', ') };
      }
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (!formData.destination || !formData.startDate || !formData.endDate || !formData.budget) {
      setError('Please fill in all required fields (Destination, Dates & Budget)');
      return;
    }

    if (new Date(formData.startDate) > new Date(formData.endDate)) {
      setError('Start date cannot be after end date');
      return;
    }

    try {
      setLoading(true);
      const createdTrip = await tripService.createTrip({
        ...formData,
        budget: parseFloat(formData.budget),
      });

      showToast('Trip created successfully! Finding compatible travel buddies...', 'success');
      // Redirect straight to BuddyMatches for this trip
      navigate(`/matches?tripId=${createdTrip.id}`);
    } catch (err) {
      setError(err.message || 'Failed to create trip');
      showToast(err.message || 'Failed to create trip', 'error');
    } finally {
      setLoading(false);
    }
  };

  const popularInterests = ['Beaches', 'Local Food', 'Photography', 'Heritage', 'Trekking', 'Cafes', 'Sunsets', 'Yoga', 'Scuba Diving'];

  return (
    <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
      <div className="bg-white rounded-3xl p-8 sm:p-10 shadow-xl border border-slate-100 space-y-8">
        <div>
          <span className="text-xs font-bold text-emerald-700 uppercase tracking-widest bg-emerald-50 px-3 py-1 rounded-full">
            Plan a Journey
          </span>
          <h1 className="text-2xl sm:text-3xl font-extrabold text-slate-900 tracking-tight mt-2">
            Create Your Next Trip
          </h1>
          <p className="text-xs sm:text-sm text-slate-500 mt-1">
            Share your travel details to discover ranked compatible travel companions and local stays.
          </p>
        </div>

        {error && (
          <div className="p-3.5 rounded-xl bg-red-50 border border-red-200 text-red-800 text-xs font-semibold">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-6">
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-5">
            {/* Destination */}
            <div className="space-y-1.5 sm:col-span-2">
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider">
                Destination *
              </label>
              <div className="relative">
                <MapPin className="w-5 h-5 text-emerald-600 absolute left-3.5 top-3" />
                <input
                  type="text"
                  name="destination"
                  value={formData.destination}
                  onChange={handleChange}
                  placeholder="e.g. Goa, Manali, Jaipur"
                  required
                  className="w-full pl-11 pr-4 py-2.5 rounded-xl border border-slate-200 focus:border-emerald-600 focus:ring-2 focus:ring-emerald-600/20 text-sm outline-none font-medium"
                />
              </div>
            </div>

            {/* Start Date */}
            <div className="space-y-1.5">
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider">
                Start Date *
              </label>
              <div className="relative">
                <Calendar className="w-4 h-4 text-slate-400 absolute left-3.5 top-3" />
                <input
                  type="date"
                  name="startDate"
                  value={formData.startDate}
                  onChange={handleChange}
                  required
                  className="w-full pl-10 pr-3 py-2.5 rounded-xl border border-slate-200 focus:border-emerald-600 focus:ring-2 focus:ring-emerald-600/20 text-sm outline-none"
                />
              </div>
            </div>

            {/* End Date */}
            <div className="space-y-1.5">
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider">
                End Date *
              </label>
              <div className="relative">
                <Calendar className="w-4 h-4 text-slate-400 absolute left-3.5 top-3" />
                <input
                  type="date"
                  name="endDate"
                  value={formData.endDate}
                  onChange={handleChange}
                  required
                  className="w-full pl-10 pr-3 py-2.5 rounded-xl border border-slate-200 focus:border-emerald-600 focus:ring-2 focus:ring-emerald-600/20 text-sm outline-none"
                />
              </div>
            </div>

            {/* Budget */}
            <div className="space-y-1.5">
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider">
                Estimated Budget (₹) *
              </label>
              <div className="relative">
                <span className="font-bold text-slate-400 absolute left-3.5 top-2.5 text-sm">₹</span>
                <input
                  type="number"
                  name="budget"
                  value={formData.budget}
                  onChange={handleChange}
                  placeholder="e.g. 12000"
                  min="500"
                  required
                  className="w-full pl-8 pr-3 py-2.5 rounded-xl border border-slate-200 focus:border-emerald-600 focus:ring-2 focus:ring-emerald-600/20 text-sm outline-none font-medium"
                />
              </div>
            </div>

            {/* Travel Style */}
            <div className="space-y-1.5">
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider">
                Travel Style *
              </label>
              <select
                name="travelStyle"
                value={formData.travelStyle}
                onChange={handleChange}
                className="w-full px-3 py-2.5 rounded-xl border border-slate-200 focus:border-emerald-600 focus:ring-2 focus:ring-emerald-600/20 text-sm outline-none bg-white font-medium"
              >
                <option value="SOLO">Solo Traveler</option>
                <option value="BACKPACKER">Backpacker</option>
                <option value="BUDGET">Budget Friendly</option>
                <option value="ADVENTURE">Adventure & Trekking</option>
                <option value="CULTURAL">Cultural & Heritage</option>
                <option value="LUXURY">Comfort & Leisure</option>
              </select>
            </div>
          </div>

          {/* Interests */}
          <div className="space-y-2">
            <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider">
              Interests & Activities (Click to add or type below)
            </label>
            <div className="flex flex-wrap gap-2 mb-2">
              {popularInterests.map((interest) => {
                const isSelected = formData.interests.includes(interest);
                return (
                  <button
                    key={interest}
                    type="button"
                    onClick={() => handleInterestToggle(interest)}
                    className={`px-3 py-1 rounded-full text-xs font-medium transition-colors ${
                      isSelected
                        ? 'bg-emerald-700 text-white'
                        : 'bg-slate-100 text-slate-700 hover:bg-slate-200'
                    }`}
                  >
                    {isSelected ? `✓ ${interest}` : `+ ${interest}`}
                  </button>
                );
              })}
            </div>
            <input
              type="text"
              name="interests"
              value={formData.interests}
              onChange={handleChange}
              placeholder="e.g. Beaches, Photography, Local Food, Sunsets"
              className="w-full px-3 py-2.5 rounded-xl border border-slate-200 focus:border-emerald-600 focus:ring-2 focus:ring-emerald-600/20 text-sm outline-none"
            />
          </div>

          {/* Description */}
          <div className="space-y-1.5">
            <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider">
              Trip Description & What You're Looking For
            </label>
            <textarea
              name="description"
              value={formData.description}
              onChange={handleChange}
              rows="4"
              placeholder="e.g. Planning a relaxed solo trip to Goa to explore coastal cliffs, sample local fish curry thalis, and share scooter rental costs with like-minded companions."
              className="w-full p-3 rounded-xl border border-slate-200 focus:border-emerald-600 focus:ring-2 focus:ring-emerald-600/20 text-sm outline-none"
            />
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full py-3.5 rounded-xl bg-emerald-700 hover:bg-emerald-800 text-white font-bold text-sm transition-colors shadow-md flex items-center justify-center space-x-2 disabled:opacity-50"
          >
            {loading ? (
              <>
                <Loader2 className="w-4 h-4 animate-spin" />
                <span>Posting trip & calculating buddy matches...</span>
              </>
            ) : (
              <>
                <span>Publish Trip & Find Travel Buddies</span>
                <ArrowRight className="w-4 h-4" />
              </>
            )}
          </button>
        </form>
      </div>
    </div>
  );
};

export default CreateTrip;
