import React, { useState, useEffect } from 'react';
import { communityService } from '../services/communityService';
import { useToast } from '../context/ToastContext';
import LoadingSpinner from '../components/LoadingSpinner';
import Modal from '../components/Modal';
import { Utensils, MapPin, Search, Calendar, Heart, Send } from 'lucide-react';

const FoodExperiences = () => {
  const [foods, setFoods] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchLocation, setSearchLocation] = useState('');
  const [searchCuisine, setSearchCuisine] = useState('');

  // Reserve Modal
  const [selectedFood, setSelectedFood] = useState(null);
  const [reserveModalOpen, setReserveModalOpen] = useState(false);
  const [partySize, setPartySize] = useState('1');
  const [reserveDate, setReserveDate] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const { showToast } = useToast();

  const fetchFoods = async () => {
    try {
      setLoading(true);
      const data = await communityService.getFoodListings(searchLocation, searchCuisine);
      setFoods(data);
    } catch (err) {
      console.error('Error fetching food experiences:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchFoods();
  }, [searchLocation, searchCuisine]);

  const handleOpenReserve = (food) => {
    setSelectedFood(food);
    setReserveModalOpen(true);
  };

  const handleReserveSubmit = (e) => {
    e.preventDefault();
    setSubmitting(true);
    setTimeout(() => {
      setSubmitting(false);
      setReserveModalOpen(false);
      showToast(`Reservation request sent to provider ${selectedFood?.providerName || 'Local Family'}!`, 'success');
    }, 600);
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      {/* Banner */}
      <div className="bg-gradient-to-r from-orange-800 via-amber-800 to-amber-900 rounded-3xl p-8 text-white shadow-lg space-y-4">
        <div className="inline-flex items-center space-x-2 text-xs font-bold uppercase tracking-wider bg-white/10 px-3 py-1 rounded-full">
          <Utensils className="w-3.5 h-3.5" />
          <span>Local Home Kitchens & Dining</span>
        </div>
        <h1 className="text-3xl sm:text-4xl font-extrabold tracking-tight">
          Homemade Food Experiences
        </h1>
        <p className="text-xs sm:text-sm text-amber-100 max-w-2xl leading-relaxed">
          Dine with local families and taste generations-old home recipes you can never find in commercial restaurants. Authentic, hearty and sustainable.
        </p>

        {/* Filters */}
        <div className="pt-2 flex flex-col sm:flex-row gap-3">
          <div className="flex items-center space-x-2 bg-white text-slate-800 px-4 py-2.5 rounded-xl flex-1 border border-white/20 shadow-sm">
            <Search className="w-4 h-4 text-orange-600 flex-shrink-0" />
            <input
              type="text"
              value={searchLocation}
              onChange={(e) => setSearchLocation(e.target.value)}
              placeholder="Search by location (e.g. Goa, Anjuna, Manali)..."
              className="w-full bg-transparent text-xs focus:outline-none font-medium"
            />
          </div>
          <div className="flex items-center space-x-2 bg-white text-slate-800 px-4 py-2.5 rounded-xl sm:w-60 border border-white/20 shadow-sm">
            <span className="text-xs font-bold text-slate-400">Cuisine:</span>
            <input
              type="text"
              value={searchCuisine}
              onChange={(e) => setSearchCuisine(e.target.value)}
              placeholder="e.g. Seafood, Bakery"
              className="w-full bg-transparent text-xs focus:outline-none font-medium"
            />
          </div>
        </div>
      </div>

      {loading ? (
        <LoadingSpinner text="Loading culinary experiences..." />
      ) : foods.length === 0 ? (
        <div className="p-12 text-center bg-white rounded-3xl border border-slate-100 space-y-3">
          <Utensils className="w-10 h-10 text-slate-300 mx-auto" />
          <h3 className="text-base font-bold text-slate-800">No food experiences found</h3>
          <p className="text-xs text-slate-500">Try adjusting your search criteria or clearing filters.</p>
          <button
            onClick={() => { setSearchLocation(''); setSearchCuisine(''); }}
            className="px-4 py-2 rounded-xl bg-orange-700 text-white text-xs font-semibold"
          >
            Clear Filters
          </button>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {foods.map((food) => (
            <div
              key={food.id}
              className="bg-white rounded-3xl border border-slate-100 overflow-hidden shadow-sm hover:shadow-xl transition-all flex flex-col justify-between"
            >
              <div>
                <div className="relative h-48 overflow-hidden">
                  <img
                    src={food.imageUrl || "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&auto=format&fit=crop&q=80"}
                    alt={food.foodName}
                    className="w-full h-full object-cover hover:scale-105 transition-transform duration-300"
                  />
                  <div className="absolute top-3 right-3 bg-white/95 backdrop-blur-md px-3 py-1 rounded-full text-xs font-extrabold text-slate-900 shadow-md">
                    ₹{food.price} <span className="text-[10px] text-slate-500 font-normal">/ person</span>
                  </div>
                </div>

                <div className="p-5 space-y-3">
                  <div className="flex items-center justify-between">
                    <span className="text-xs font-semibold text-orange-800 bg-orange-50 px-2.5 py-0.5 rounded-full border border-orange-100">
                      {food.cuisine}
                    </span>
                    <span className="text-xs text-slate-400 flex items-center space-x-1">
                      <MapPin className="w-3 h-3 text-orange-500" />
                      <span>{food.location}</span>
                    </span>
                  </div>

                  <h3 className="font-extrabold text-slate-900 text-lg leading-snug">
                    {food.foodName}
                  </h3>

                  <p className="text-xs text-slate-600 leading-relaxed line-clamp-3">
                    {food.description}
                  </p>

                  {food.providerName && (
                    <div className="flex items-center space-x-2 pt-2 border-t border-slate-100 text-xs text-slate-600">
                      <div className="w-6 h-6 rounded-full bg-orange-100 text-orange-800 flex items-center justify-center font-bold text-[10px]">
                        {food.providerName.charAt(0)}
                      </div>
                      <span>Prepared by <strong className="text-slate-800">{food.providerName}</strong></span>
                    </div>
                  )}

                  {food.availableDays && (
                    <p className="text-[11px] text-slate-400 flex items-center space-x-1">
                      <Calendar className="w-3 h-3 text-slate-400" />
                      <span>{food.availableDays}</span>
                    </p>
                  )}
                </div>
              </div>

              <div className="p-5 pt-0">
                <button
                  onClick={() => handleOpenReserve(food)}
                  className="w-full py-2.5 rounded-xl bg-orange-600 hover:bg-orange-700 text-white font-bold text-xs transition-colors shadow-sm flex items-center justify-center space-x-1.5"
                >
                  <Utensils className="w-3.5 h-3.5" />
                  <span>Reserve Table / Connect Host</span>
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Reservation Modal */}
      <Modal
        isOpen={reserveModalOpen}
        onClose={() => setReserveModalOpen(false)}
        title={`Reserve Food Experience: ${selectedFood?.foodName}`}
      >
        <form onSubmit={handleReserveSubmit} className="space-y-4">
          <div className="p-3 bg-amber-50 rounded-xl text-xs space-y-1 border border-amber-200">
            <p className="font-bold text-slate-900">{selectedFood?.foodName} ({selectedFood?.cuisine})</p>
            <p className="text-slate-600">Location: {selectedFood?.location} • Rate: ₹{selectedFood?.price}/person</p>
            <p className="text-orange-800 font-semibold">Available: {selectedFood?.availableDays || 'Everyday'}</p>
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div className="space-y-1">
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider">
                Travelers Count
              </label>
              <input
                type="number"
                min="1"
                max="10"
                value={partySize}
                onChange={(e) => setPartySize(e.target.value)}
                required
                className="w-full p-2.5 rounded-xl border border-slate-200 text-xs outline-none"
              />
            </div>
            <div className="space-y-1">
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider">
                Preferred Date
              </label>
              <input
                type="date"
                value={reserveDate}
                onChange={(e) => setReserveDate(e.target.value)}
                required
                className="w-full p-2.5 rounded-xl border border-slate-200 text-xs outline-none"
              />
            </div>
          </div>

          <button
            type="submit"
            disabled={submitting}
            className="w-full py-3 rounded-xl bg-orange-600 hover:bg-orange-700 text-white font-bold text-xs transition-colors shadow-md flex items-center justify-center space-x-1.5 disabled:opacity-50"
          >
            {submitting ? (
              <span>Submitting request...</span>
            ) : (
              <>
                <Send className="w-3.5 h-3.5" />
                <span>Confirm Food Inquiry</span>
              </>
            )}
          </button>
        </form>
      </Modal>
    </div>
  );
};

export default FoodExperiences;
