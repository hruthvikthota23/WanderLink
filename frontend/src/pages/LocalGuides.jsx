import React, { useState, useEffect } from 'react';
import { communityService } from '../services/communityService';
import { useToast } from '../context/ToastContext';
import LoadingSpinner from '../components/LoadingSpinner';
import Modal from '../components/Modal';
import { Flag, Star, MapPin, Search, Mail, Phone, Award, Send } from 'lucide-react';

const LocalGuides = () => {
  const [guides, setGuides] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchDest, setSearchDest] = useState('');

  // Guide Contact Modal
  const [selectedGuide, setSelectedGuide] = useState(null);
  const [modalOpen, setModalOpen] = useState(false);
  const [guideMessage, setGuideMessage] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const { showToast } = useToast();

  const fetchGuides = async () => {
    try {
      setLoading(true);
      const data = await communityService.getGuides(searchDest);
      setGuides(data);
    } catch (err) {
      console.error('Error fetching guides:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchGuides();
  }, [searchDest]);

  const handleOpenContact = (guide) => {
    setSelectedGuide(guide);
    setGuideMessage(`Hi ${guide.guideName}! I will be visiting ${guide.destination} soon and am interested in your walking/heritage tour. Are you available for a day?`);
    setModalOpen(true);
  };

  const handleSendInquiry = (e) => {
    e.preventDefault();
    setSubmitting(true);
    setTimeout(() => {
      setSubmitting(false);
      setModalOpen(false);
      showToast(`Inquiry sent to ${selectedGuide?.guideName}! They will get in touch with you.`, 'success');
    }, 600);
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      {/* Banner */}
      <div className="bg-gradient-to-r from-teal-800 via-emerald-800 to-slate-900 rounded-3xl p-8 text-white shadow-lg space-y-4">
        <div className="inline-flex items-center space-x-2 text-xs font-bold uppercase tracking-wider bg-white/10 px-3 py-1 rounded-full">
          <Flag className="w-3.5 h-3.5" />
          <span>Local Storytellers & Trek Leaders</span>
        </div>
        <h1 className="text-3xl sm:text-4xl font-extrabold tracking-tight">
          Certified Local Guides
        </h1>
        <p className="text-xs sm:text-sm text-teal-100 max-w-2xl leading-relaxed">
          Discover hidden viewpoints, cultural history, secret waterfalls and forgotten heritage through the eyes of certified local residents.
        </p>

        {/* Search */}
        <div className="pt-2 max-w-md">
          <div className="flex items-center space-x-2 bg-white text-slate-800 px-4 py-2.5 rounded-xl border border-white/20 shadow-sm">
            <Search className="w-4 h-4 text-teal-600 flex-shrink-0" />
            <input
              type="text"
              value={searchDest}
              onChange={(e) => setSearchDest(e.target.value)}
              placeholder="Search guide by destination (e.g. Goa, Manali, Jaipur)..."
              className="w-full bg-transparent text-xs focus:outline-none font-medium"
            />
          </div>
        </div>
      </div>

      {loading ? (
        <LoadingSpinner text="Finding certified local guides..." />
      ) : guides.length === 0 ? (
        <div className="p-12 text-center bg-white rounded-3xl border border-slate-100 space-y-3">
          <Flag className="w-10 h-10 text-slate-300 mx-auto" />
          <h3 className="text-base font-bold text-slate-800">No guides found for this destination</h3>
          <p className="text-xs text-slate-500">Try searching for Goa, Manali, or Jaipur.</p>
          <button
            onClick={() => setSearchDest('')}
            className="px-4 py-2 rounded-xl bg-teal-700 text-white text-xs font-semibold"
          >
            Clear Search
          </button>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {guides.map((guide) => (
            <div
              key={guide.id}
              className="bg-white rounded-3xl border border-slate-100 p-6 shadow-sm hover:shadow-xl transition-all flex flex-col justify-between space-y-4"
            >
              <div className="space-y-4">
                <div className="flex items-center space-x-4">
                  <img
                    src={guide.imageUrl || "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=400&auto=format&fit=crop&q=80"}
                    alt={guide.guideName}
                    className="w-16 h-16 rounded-2xl object-cover border-2 border-teal-500 shadow-sm flex-shrink-0"
                  />
                  <div>
                    <div className="flex items-center space-x-1.5 text-amber-500 text-xs font-bold">
                      <Star className="w-4 h-4 fill-current" />
                      <span>{guide.rating || 5.0}</span>
                      <span className="text-slate-400 font-normal">({guide.experienceYears || 5}+ yrs exp)</span>
                    </div>
                    <h3 className="font-extrabold text-slate-900 text-lg leading-tight mt-0.5">
                      {guide.guideName}
                    </h3>
                    <p className="text-xs text-teal-700 font-semibold flex items-center space-x-1 mt-0.5">
                      <MapPin className="w-3.5 h-3.5" />
                      <span>{guide.destination}</span>
                    </p>
                  </div>
                </div>

                <p className="text-xs text-slate-600 leading-relaxed line-clamp-3">
                  {guide.bio}
                </p>

                <div className="text-xs space-y-1 bg-slate-50 p-3 rounded-xl border border-slate-100">
                  <p className="text-slate-500">
                    Languages: <strong className="text-slate-700">{guide.languages || 'English, Hindi'}</strong>
                  </p>
                  <p className="text-slate-500">
                    Rate: <strong className="text-emerald-700 font-extrabold">₹{guide.pricePerDay} / day</strong>
                  </p>
                </div>
              </div>

              <div>
                <button
                  onClick={() => handleOpenContact(guide)}
                  className="w-full py-2.5 rounded-xl bg-teal-700 hover:bg-teal-800 text-white font-bold text-xs transition-colors shadow-sm flex items-center justify-center space-x-1.5"
                >
                  <Mail className="w-3.5 h-3.5" />
                  <span>Book / Contact Guide</span>
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Contact Guide Modal */}
      <Modal
        isOpen={modalOpen}
        onClose={() => setModalOpen(false)}
        title={`Contact Guide: ${selectedGuide?.guideName}`}
      >
        <form onSubmit={handleSendInquiry} className="space-y-4">
          <div className="p-3 bg-teal-50 rounded-xl text-xs space-y-1 border border-teal-200">
            <p className="font-bold text-slate-900">{selectedGuide?.guideName}</p>
            <p className="text-slate-600">Location: {selectedGuide?.destination} • Rate: ₹{selectedGuide?.pricePerDay}/day</p>
            {selectedGuide?.contactEmail && (
              <p className="text-teal-800 font-medium">Direct Email: {selectedGuide.contactEmail}</p>
            )}
            {selectedGuide?.contactPhone && (
              <p className="text-teal-800 font-medium">Direct Phone: {selectedGuide.contactPhone}</p>
            )}
          </div>

          <div className="space-y-1">
            <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider">
              Message to Guide
            </label>
            <textarea
              value={guideMessage}
              onChange={(e) => setGuideMessage(e.target.value)}
              rows="4"
              required
              className="w-full p-3 rounded-xl border border-slate-200 focus:border-teal-600 focus:ring-2 focus:ring-teal-600/20 text-xs outline-none"
            />
          </div>

          <button
            type="submit"
            disabled={submitting}
            className="w-full py-3 rounded-xl bg-teal-700 hover:bg-teal-800 text-white font-bold text-xs transition-colors shadow-md flex items-center justify-center space-x-1.5 disabled:opacity-50"
          >
            {submitting ? (
              <span>Sending inquiry...</span>
            ) : (
              <>
                <Send className="w-3.5 h-3.5" />
                <span>Send Message to Guide</span>
              </>
            )}
          </button>
        </form>
      </Modal>
    </div>
  );
};

export default LocalGuides;
