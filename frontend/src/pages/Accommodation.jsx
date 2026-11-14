import React, { useState, useEffect } from 'react';
import { communityService } from '../services/communityService';
import { useToast } from '../context/ToastContext';
import LoadingSpinner from '../components/LoadingSpinner';
import Modal from '../components/Modal';
import { Bed, MapPin, Search, User, CheckCircle2, Phone, Mail, Send } from 'lucide-react';

const Accommodation = () => {
  const [accommodations, setAccommodations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchLocation, setSearchLocation] = useState('');
  const [maxPrice, setMaxPrice] = useState('');

  // Contact Modal
  const [selectedStay, setSelectedStay] = useState(null);
  const [modalOpen, setModalOpen] = useState(false);
  const [inquiryMessage, setInquiryMessage] = useState('');
  const [submittingInquiry, setSubmittingInquiry] = useState(false);

  const { showToast } = useToast();

  const fetchStays = async () => {
    try {
      setLoading(true);
      const data = await communityService.getAccommodations(searchLocation, maxPrice ? parseFloat(maxPrice) : null);
      setAccommodations(data);
    } catch (err) {
      console.error('Error fetching accommodations:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchStays();
  }, [searchLocation, maxPrice]);

  const handleOpenContact = (stay) => {
    setSelectedStay(stay);
    setInquiryMessage(`Hello! I'm planning to visit ${stay.location} and would love to stay at ${stay.propertyName}. Are you open for travelers next month?`);
    setModalOpen(true);
  };

  const handleSendInquiry = (e) => {
    e.preventDefault();
    setSubmittingInquiry(true);
    setTimeout(() => {
      setSubmittingInquiry(false);
      setModalOpen(false);
      showToast(`Inquiry sent to host ${selectedStay?.hostName || 'Maria D\'Souza'}! They will respond shortly.`, 'success');
    }, 600);
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      {/* Header Banner */}
      <div className="bg-gradient-to-r from-emerald-800 to-teal-700 rounded-3xl p-8 text-white shadow-lg space-y-4">
        <div className="inline-flex items-center space-x-2 text-xs font-bold uppercase tracking-wider bg-white/10 px-3 py-1 rounded-full">
          <Bed className="w-3.5 h-3.5" />
          <span>Local Community Lodging</span>
        </div>
        <h1 className="text-3xl sm:text-4xl font-extrabold tracking-tight">
          Local Homestays & Heritage Villas
        </h1>
        <p className="text-xs sm:text-sm text-emerald-100 max-w-2xl leading-relaxed">
          Stay directly with local families, village hosts, and heritage villa owners. Support sustainable community tourism and authentic local experiences.
        </p>

        {/* Filter controls */}
        <div className="pt-2 flex flex-col sm:flex-row gap-3">
          <div className="flex items-center space-x-2 bg-white text-slate-800 px-4 py-2.5 rounded-xl flex-1 border border-white/20 shadow-sm">
            <Search className="w-4 h-4 text-emerald-600 flex-shrink-0" />
            <input
              type="text"
              value={searchLocation}
              onChange={(e) => setSearchLocation(e.target.value)}
              placeholder="Search location (e.g. Goa, Manali, Panaji)..."
              className="w-full bg-transparent text-xs focus:outline-none font-medium"
            />
          </div>
          <div className="flex items-center space-x-2 bg-white text-slate-800 px-4 py-2.5 rounded-xl sm:w-60 border border-white/20 shadow-sm">
            <span className="text-xs font-bold text-slate-400">Max ₹:</span>
            <input
              type="number"
              value={maxPrice}
              onChange={(e) => setMaxPrice(e.target.value)}
              placeholder="e.g. 2000"
              className="w-full bg-transparent text-xs focus:outline-none font-medium"
            />
          </div>
        </div>
      </div>

      {loading ? (
        <LoadingSpinner text="Loading local homestays..." />
      ) : accommodations.length === 0 ? (
        <div className="p-12 text-center bg-white rounded-3xl border border-slate-100 space-y-3">
          <Bed className="w-10 h-10 text-slate-300 mx-auto" />
          <h3 className="text-base font-bold text-slate-800">No homestays found for this location/budget</h3>
          <p className="text-xs text-slate-500">Try resetting filters to explore all available listings.</p>
          <button
            onClick={() => { setSearchLocation(''); setMaxPrice(''); }}
            className="px-4 py-2 rounded-xl bg-emerald-700 text-white text-xs font-semibold"
          >
            Clear Filters
          </button>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {accommodations.map((stay) => (
            <div
              key={stay.id}
              className="bg-white rounded-3xl border border-slate-100 overflow-hidden shadow-sm hover:shadow-xl transition-all flex flex-col justify-between"
            >
              <div>
                <div className="relative h-52 overflow-hidden">
                  <img
                    src={stay.imageUrl || "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=600&auto=format&fit=crop&q=80"}
                    alt={stay.propertyName}
                    className="w-full h-full object-cover hover:scale-105 transition-transform duration-300"
                  />
                  <div className="absolute top-3 right-3 bg-white/95 backdrop-blur-md px-3 py-1 rounded-full text-xs font-extrabold text-slate-900 shadow-md">
                    ₹{stay.pricePerNight?.toLocaleString()} <span className="text-[10px] text-slate-500 font-normal">/ night</span>
                  </div>
                </div>

                <div className="p-5 space-y-3">
                  <div className="flex items-center space-x-1.5 text-xs text-emerald-700 font-semibold">
                    <MapPin className="w-3.5 h-3.5" />
                    <span>{stay.location}</span>
                  </div>

                  <h3 className="font-extrabold text-slate-900 text-lg leading-snug">
                    {stay.propertyName}
                  </h3>

                  <p className="text-xs text-slate-600 leading-relaxed line-clamp-3">
                    {stay.description}
                  </p>

                  {stay.hostName && (
                    <div className="flex items-center space-x-2 pt-2 border-t border-slate-100 text-xs text-slate-600">
                      <div className="w-6 h-6 rounded-full bg-emerald-100 text-emerald-800 flex items-center justify-center font-bold text-[10px]">
                        {stay.hostName.charAt(0)}
                      </div>
                      <span>Hosted by <strong className="text-slate-800">{stay.hostName}</strong></span>
                    </div>
                  )}

                  {stay.amenities && (
                    <div className="flex flex-wrap gap-1.5 pt-1">
                      {stay.amenities.split(',').map((amenity, idx) => (
                        <span
                          key={idx}
                          className="text-[10px] font-medium px-2 py-0.5 rounded-md bg-slate-100 text-slate-600"
                        >
                          {amenity.trim()}
                        </span>
                      ))}
                    </div>
                  )}
                </div>
              </div>

              <div className="p-5 pt-0">
                <button
                  onClick={() => handleOpenContact(stay)}
                  className="w-full py-2.5 rounded-xl bg-emerald-700 hover:bg-emerald-800 text-white font-bold text-xs transition-colors shadow-sm flex items-center justify-center space-x-1.5"
                >
                  <Mail className="w-4 h-4" />
                  <span>Contact Host / Inquire</span>
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Host Inquiry Modal */}
      <Modal
        isOpen={modalOpen}
        onClose={() => setModalOpen(false)}
        title={`Contact Host: ${selectedStay?.propertyName}`}
      >
        <form onSubmit={handleSendInquiry} className="space-y-4">
          <div className="p-3 bg-slate-50 rounded-xl text-xs space-y-1 border border-slate-100">
            <p className="font-bold text-slate-900">{selectedStay?.propertyName}</p>
            <p className="text-slate-500">Location: {selectedStay?.location} • Rate: ₹{selectedStay?.pricePerNight}/night</p>
            {selectedStay?.contactInfo && (
              <p className="text-emerald-700 font-medium">Host Contact: {selectedStay.contactInfo}</p>
            )}
          </div>

          <div className="space-y-1">
            <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider">
              Your Message to Host
            </label>
            <textarea
              value={inquiryMessage}
              onChange={(e) => setInquiryMessage(e.target.value)}
              rows="4"
              required
              className="w-full p-3 rounded-xl border border-slate-200 focus:border-emerald-600 focus:ring-2 focus:ring-emerald-600/20 text-xs outline-none"
            />
          </div>

          <button
            type="submit"
            disabled={submittingInquiry}
            className="w-full py-3 rounded-xl bg-emerald-700 hover:bg-emerald-800 text-white font-bold text-xs transition-colors shadow-md flex items-center justify-center space-x-1.5 disabled:opacity-50"
          >
            {submittingInquiry ? (
              <span>Sending inquiry...</span>
            ) : (
              <>
                <Send className="w-3.5 h-3.5" />
                <span>Send Inquiry to Host</span>
              </>
            )}
          </button>
        </form>
      </Modal>
    </div>
  );
};

export default Accommodation;
