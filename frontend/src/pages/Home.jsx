import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { 
  Compass, Users, MapPin, Sparkles, Bed, Utensils, Flag, 
  ArrowRight, ShieldCheck, Heart, Search, Calendar, Star,
  CheckCircle, Globe, Coffee, ChevronRight, Award
} from 'lucide-react';

const Home = () => {
  const [searchDestination, setSearchDestination] = useState('');
  const navigate = useNavigate();

  const handleSearch = (e) => {
    e.preventDefault();
    if (searchDestination.trim()) {
      navigate(`/explore?destination=${encodeURIComponent(searchDestination.trim())}`);
    } else {
      navigate('/explore');
    }
  };

  return (
    <div className="space-y-16 lg:space-y-24 pb-16">
      {/* 1 & 2. Hero Section */}
      <section className="relative overflow-hidden bg-gradient-to-b from-emerald-900 via-emerald-800 to-slate-900 text-white pt-16 pb-24 lg:pt-24 lg:pb-32">
        {/* Subtle decorative background circles */}
        <div className="absolute top-0 right-0 -mt-12 -mr-12 w-96 h-96 rounded-full bg-emerald-600/20 blur-3xl pointer-events-none" />
        <div className="absolute bottom-0 left-0 -mb-16 -ml-16 w-80 h-80 rounded-full bg-orange-500/10 blur-3xl pointer-events-none" />

        <div className="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center max-w-3xl mx-auto space-y-6">
            <div className="inline-flex items-center space-x-2 px-3.5 py-1.5 rounded-full bg-emerald-700/60 border border-emerald-500/30 text-emerald-200 text-xs font-semibold tracking-wide backdrop-blur-md">
              <Sparkles className="w-3.5 h-3.5 text-amber-400" />
              <span>AI-Powered Community Travel Platform</span>
            </div>

            <h1 className="text-4xl sm:text-5xl lg:text-6xl font-extrabold tracking-tight leading-tight">
              Travel Together. <br className="hidden sm:inline" />
              <span className="text-transparent bg-clip-text bg-gradient-to-r from-emerald-300 via-teal-200 to-amber-300">
                Explore Locally.
              </span> Connect Globally.
            </h1>

            <p className="text-lg sm:text-xl text-slate-200 font-normal leading-relaxed">
              Discover compatible travel buddies, experience local culture and connect with communities wherever your journey takes you.
            </p>

            {/* 3. Destination Search Box */}
            <div className="pt-4 max-w-2xl mx-auto">
              <form onSubmit={handleSearch} className="bg-white/95 backdrop-blur-md p-2 rounded-2xl shadow-2xl flex flex-col sm:flex-row items-center gap-2 border border-white/20">
                <div className="flex items-center space-x-3 px-3 py-2 w-full text-slate-800">
                  <MapPin className="w-5 h-5 text-emerald-600 flex-shrink-0" />
                  <input
                    type="text"
                    value={searchDestination}
                    onChange={(e) => setSearchDestination(e.target.value)}
                    placeholder="Where are you traveling? (e.g. Goa, Manali, Jaipur)"
                    className="w-full bg-transparent focus:outline-none text-sm placeholder:text-slate-400 font-medium"
                  />
                </div>
                <button
                  type="submit"
                  className="w-full sm:w-auto px-6 py-3.5 rounded-xl bg-orange-600 hover:bg-orange-700 text-white font-semibold text-sm transition-all shadow-md flex items-center justify-center space-x-2 flex-shrink-0"
                >
                  <Search className="w-4 h-4" />
                  <span>Discover</span>
                </button>
              </form>

              {/* Quick tags */}
              <div className="flex flex-wrap items-center justify-center gap-2 mt-4 text-xs text-slate-300">
                <span className="font-semibold text-slate-400">Popular:</span>
                {['Goa', 'Manali', 'Kerala', 'Jaipur'].map((tag) => (
                  <button
                    key={tag}
                    onClick={() => {
                      setSearchDestination(tag);
                      navigate(`/explore?destination=${encodeURIComponent(tag)}`);
                    }}
                    className="px-2.5 py-1 rounded-full bg-white/10 hover:bg-white/20 text-slate-200 transition-colors"
                  >
                    {tag}
                  </button>
                ))}
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* 4. Find Travel Buddies Highlight Section */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="bg-gradient-to-r from-emerald-50 via-teal-50 to-emerald-100/50 rounded-3xl p-8 lg:p-12 border border-emerald-100 shadow-sm">
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-8 items-center">
            <div className="space-y-4">
              <div className="inline-flex items-center space-x-2 text-xs font-bold text-emerald-800 uppercase tracking-wider bg-emerald-200/50 px-3 py-1 rounded-full">
                <Users className="w-4 h-4 text-emerald-700" />
                <span>Smart Weighted Matching</span>
              </div>
              <h2 className="text-3xl font-extrabold text-slate-900 tracking-tight">
                Never Travel Solo Unless You Want To
              </h2>
              <p className="text-slate-600 leading-relaxed text-sm sm:text-base">
                Our intelligent matching algorithm calculates compatibility across 5 distinct dimensions: 
                <strong> Destination (30%)</strong>, <strong>Dates (25%)</strong>, <strong>Interests (20%)</strong>, <strong>Budget (15%)</strong>, and <strong>Travel Style (10%)</strong>.
              </p>
              <div className="pt-2 flex flex-wrap gap-3">
                <Link
                  to="/matches"
                  className="inline-flex items-center space-x-2 px-5 py-3 rounded-xl bg-emerald-700 text-white font-semibold text-sm hover:bg-emerald-800 transition-colors shadow-sm"
                >
                  <span>Explore Travel Matches</span>
                  <ArrowRight className="w-4 h-4" />
                </Link>
                <Link
                  to="/create-trip"
                  className="inline-flex items-center space-x-2 px-5 py-3 rounded-xl bg-white text-emerald-800 border border-emerald-200 font-semibold text-sm hover:bg-emerald-50 transition-colors"
                >
                  <span>Post Your Trip</span>
                </Link>
              </div>
            </div>

            {/* Demonstration Card Preview */}
            <div className="bg-white rounded-2xl p-6 shadow-xl border border-emerald-100 space-y-4">
              <div className="flex items-center justify-between">
                <div className="flex items-center space-x-3">
                  <img
                    src="https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=100&auto=format&fit=crop&q=80"
                    alt="Alex Rivera"
                    className="w-12 h-12 rounded-full object-cover border-2 border-emerald-500"
                  />
                  <div>
                    <h4 className="font-bold text-slate-900">Alex Rivera, 28</h4>
                    <p className="text-xs text-slate-500 flex items-center space-x-1">
                      <MapPin className="w-3 h-3 text-emerald-600" />
                      <span>Heading to Goa</span>
                    </p>
                  </div>
                </div>
                <div className="text-right">
                  <span className="inline-block px-3 py-1 rounded-full bg-emerald-100 text-emerald-800 font-extrabold text-sm">
                    92% Match
                  </span>
                  <p className="text-[10px] text-slate-400 mt-0.5">High Compatibility</p>
                </div>
              </div>
              <p className="text-xs text-slate-600 italic bg-slate-50 p-3 rounded-xl border border-slate-100">
                "Heading to Goa for a week of coastal work and weekend explorations. Want to rent scooters and share accommodation costs!"
              </p>
              <div className="flex items-center justify-between text-xs text-slate-500 pt-2 border-t border-slate-100">
                <span>Budget: ₹13,500</span>
                <span className="font-semibold text-emerald-700">Dec 11 - Dec 17</span>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* 5. Featured Destinations */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-8">
        <div className="flex flex-col sm:flex-row sm:items-end justify-between gap-4">
          <div>
            <h2 className="text-2xl sm:text-3xl font-extrabold text-slate-900 tracking-tight">
              Featured Community Hubs
            </h2>
            <p className="text-slate-500 text-sm mt-1">
              Top locations where solo travelers and local hosts are active right now.
            </p>
          </div>
          <Link to="/explore" className="text-emerald-700 hover:text-emerald-800 text-sm font-semibold flex items-center space-x-1">
            <span>View all destinations</span>
            <ChevronRight className="w-4 h-4" />
          </Link>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          {[
            {
              name: 'Goa',
              tag: 'Beach & Portuguese Heritage',
              image: 'https://images.unsplash.com/photo-1512343879784-a960bf40e7f2?w=600&auto=format&fit=crop&q=80',
              travelers: '12+ Travelers',
              stays: '4 Homestays'
            },
            {
              name: 'Manali, Himachal',
              tag: 'Mountain Treks & Apples',
              image: 'https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=600&auto=format&fit=crop&q=80',
              travelers: '8+ Travelers',
              stays: '3 Cottages'
            },
            {
              name: 'Jaipur, Rajasthan',
              tag: 'Forts & Blue Pottery',
              image: 'https://images.unsplash.com/photo-1524492412937-b28074a5d7da?w=600&auto=format&fit=crop&q=80',
              travelers: '6+ Travelers',
              stays: '5 Haveli Stays'
            },
            {
              name: 'Kerala Backwaters',
              tag: 'Canoes & Spice Groves',
              image: 'https://images.unsplash.com/photo-1602216056096-3b40cc0c9944?w=600&auto=format&fit=crop&q=80',
              travelers: '9+ Travelers',
              stays: '4 Homestays'
            }
          ].map((dest) => (
            <div
              key={dest.name}
              onClick={() => navigate(`/explore?destination=${encodeURIComponent(dest.name.split(',')[0])}`)}
              className="group cursor-pointer rounded-2xl overflow-hidden bg-white border border-slate-100 shadow-sm hover:shadow-xl transition-all"
            >
              <div className="relative h-48 overflow-hidden">
                <img
                  src={dest.image}
                  alt={dest.name}
                  className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
                />
                <div className="absolute inset-0 bg-gradient-to-t from-slate-900/80 via-transparent to-transparent" />
                <div className="absolute bottom-3 left-3 text-white">
                  <h3 className="font-bold text-lg">{dest.name}</h3>
                  <p className="text-xs text-slate-300">{dest.tag}</p>
                </div>
              </div>
              <div className="p-3.5 flex items-center justify-between text-xs text-slate-600 font-medium bg-slate-50">
                <span className="flex items-center space-x-1">
                  <Users className="w-3.5 h-3.5 text-emerald-600" />
                  <span>{dest.travelers}</span>
                </span>
                <span className="flex items-center space-x-1">
                  <Bed className="w-3.5 h-3.5 text-orange-600" />
                  <span>{dest.stays}</span>
                </span>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* 6. Local Accommodation Section */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-6">
        <div className="flex items-end justify-between">
          <div>
            <div className="inline-flex items-center space-x-2 text-xs font-bold text-emerald-800 uppercase tracking-wider bg-emerald-50 px-2.5 py-1 rounded-full mb-2">
              <Bed className="w-3.5 h-3.5 text-emerald-700" />
              <span>Stay with Local Families</span>
            </div>
            <h2 className="text-2xl sm:text-3xl font-extrabold text-slate-900 tracking-tight">
              Authentic Homestays & Cottages
            </h2>
          </div>
          <Link to="/accommodations" className="text-emerald-700 hover:text-emerald-800 text-sm font-semibold flex items-center space-x-1">
            <span>Browse all stays</span>
            <ChevronRight className="w-4 h-4" />
          </Link>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {[
            {
              title: 'Sea Breeze Heritage Homestay',
              location: 'Anjuna, Goa',
              price: '₹1,500/night',
              host: "Maria D'Souza",
              image: 'https://images.unsplash.com/photo-1566073771259-6a8506099945?w=600&auto=format&fit=crop&q=80',
              amenities: 'WiFi, Kitchen, Veranda, Garden'
            },
            {
              title: 'Fontainhas Old Quarter Villa',
              location: 'Panaji, Goa',
              price: '₹2,200/night',
              host: "Maria D'Souza",
              image: 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?w=600&auto=format&fit=crop&q=80',
              amenities: 'Heritage Decor, Dedicated Desk, AC'
            },
            {
              title: 'Green Valley Bamboo Cottage',
              location: 'Old Manali, Himachal',
              price: '₹1,100/night',
              host: 'Rohan Mehta',
              image: 'https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=600&auto=format&fit=crop&q=80',
              amenities: 'Mountain Views, Bonfire, Siddu'
            }
          ].map((stay, idx) => (
            <div key={idx} className="bg-white rounded-2xl border border-slate-100 overflow-hidden shadow-sm hover:shadow-lg transition-shadow">
              <img src={stay.image} alt={stay.title} className="h-48 w-full object-cover" />
              <div className="p-5 space-y-3">
                <div className="flex items-center justify-between">
                  <span className="text-xs font-semibold text-emerald-700 bg-emerald-50 px-2 py-0.5 rounded-md">
                    {stay.location}
                  </span>
                  <span className="font-extrabold text-slate-900 text-base">{stay.price}</span>
                </div>
                <h3 className="font-bold text-slate-900 text-lg">{stay.title}</h3>
                <p className="text-xs text-slate-500">Hosted by <strong className="text-slate-700">{stay.host}</strong></p>
                <p className="text-xs text-slate-400">{stay.amenities}</p>
                <Link
                  to="/accommodations"
                  className="block text-center w-full py-2.5 rounded-xl border border-emerald-700 text-emerald-800 font-semibold text-xs hover:bg-emerald-50 transition-colors"
                >
                  View Stay Details
                </Link>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* 7. Local Food Experiences */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-6">
        <div className="flex items-end justify-between">
          <div>
            <div className="inline-flex items-center space-x-2 text-xs font-bold text-orange-800 uppercase tracking-wider bg-orange-50 px-2.5 py-1 rounded-full mb-2">
              <Utensils className="w-3.5 h-3.5 text-orange-600" />
              <span>Homemade Culinary Exchange</span>
            </div>
            <h2 className="text-2xl sm:text-3xl font-extrabold text-slate-900 tracking-tight">
              Eat with Local Families
            </h2>
          </div>
          <Link to="/food" className="text-orange-700 hover:text-orange-800 text-sm font-semibold flex items-center space-x-1">
            <span>Browse food listings</span>
            <ChevronRight className="w-4 h-4" />
          </Link>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {[
            {
              name: 'Authentic Goan Fish Curry Thali',
              cuisine: 'Goan Seafood',
              location: 'Anjuna, Goa',
              price: '₹250',
              image: 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&auto=format&fit=crop&q=80',
              desc: 'Fresh morning catch from Chapora harbor, coconut-kokum curry, kismoor, and sol kadi.'
            },
            {
              name: 'Pao, Poi & Morning Chai Breakfast',
              cuisine: 'Goan Bakery',
              location: 'Panaji, Goa',
              price: '₹120',
              image: 'https://images.unsplash.com/photo-1525351484163-7529414344d8?w=600&auto=format&fit=crop&q=80',
              desc: 'Wood-fired Poi bread served with spicy mushroom xacuti and ginger chai.'
            },
            {
              name: 'Himachali Siddu & Desi Ghee Feast',
              cuisine: 'Himachali Traditional',
              location: 'Old Manali, Himachal',
              price: '₹180',
              image: 'https://images.unsplash.com/photo-1589302168068-964664d93dc0?w=600&auto=format&fit=crop&q=80',
              desc: 'Fermented wheat buns filled with spiced poppy seeds and warm mountain ghee.'
            }
          ].map((dish, idx) => (
            <div key={idx} className="bg-white rounded-2xl border border-slate-100 overflow-hidden shadow-sm hover:shadow-lg transition-shadow">
              <img src={dish.image} alt={dish.name} className="h-44 w-full object-cover" />
              <div className="p-5 space-y-2.5">
                <div className="flex items-center justify-between">
                  <span className="text-xs font-semibold text-orange-700 bg-orange-50 px-2 py-0.5 rounded-md">
                    {dish.cuisine}
                  </span>
                  <span className="font-extrabold text-slate-900">{dish.price}</span>
                </div>
                <h3 className="font-bold text-slate-900 text-base">{dish.name}</h3>
                <p className="text-xs text-slate-500 leading-relaxed">{dish.desc}</p>
                <Link
                  to="/food"
                  className="block text-center w-full py-2.5 rounded-xl bg-orange-50 text-orange-800 font-semibold text-xs hover:bg-orange-100 transition-colors"
                >
                  Reserve Taste Experience
                </Link>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* 8. Explore with Locals (Guides) */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-6">
        <div className="flex items-end justify-between">
          <div>
            <div className="inline-flex items-center space-x-2 text-xs font-bold text-teal-800 uppercase tracking-wider bg-teal-50 px-2.5 py-1 rounded-full mb-2">
              <Flag className="w-3.5 h-3.5 text-teal-600" />
              <span>Certified Storytellers</span>
            </div>
            <h2 className="text-2xl sm:text-3xl font-extrabold text-slate-900 tracking-tight">
              Explore with Local Guides
            </h2>
          </div>
          <Link to="/guides" className="text-teal-700 hover:text-teal-800 text-sm font-semibold flex items-center space-x-1">
            <span>Meet all guides</span>
            <ChevronRight className="w-4 h-4" />
          </Link>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {[
            {
              name: 'Savio Fernandes',
              dest: 'Goa',
              rate: '₹1,200/day',
              exp: '8 Years',
              languages: 'English, Konkani, Hindi, Portuguese',
              image: 'https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=400&auto=format&fit=crop&q=80',
              rating: '4.9 (42 reviews)'
            },
            {
              name: 'Arjun Rao',
              dest: 'Manali, Himachal',
              rate: '₹1,500/day',
              exp: '6 Years',
              languages: 'English, Hindi, Pahadi',
              image: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&auto=format&fit=crop&q=80',
              rating: '4.8 (35 reviews)'
            },
            {
              name: 'Sunita Sharma',
              dest: 'Jaipur, Rajasthan',
              rate: '₹1,400/day',
              exp: '10 Years',
              languages: 'English, Hindi, French',
              image: 'https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=400&auto=format&fit=crop&q=80',
              rating: '5.0 (58 reviews)'
            }
          ].map((guide, idx) => (
            <div key={idx} className="bg-white rounded-2xl border border-slate-100 p-5 shadow-sm hover:shadow-lg transition-all flex space-x-4">
              <img src={guide.image} alt={guide.name} className="w-20 h-20 rounded-2xl object-cover border border-slate-100 flex-shrink-0" />
              <div className="space-y-1 text-xs">
                <div className="flex items-center space-x-1 text-amber-500 font-bold">
                  <Star className="w-3.5 h-3.5 fill-current" />
                  <span>{guide.rating}</span>
                </div>
                <h3 className="font-bold text-slate-900 text-base">{guide.name}</h3>
                <p className="text-slate-500 font-medium">Guide in <strong className="text-slate-700">{guide.dest}</strong></p>
                <p className="text-slate-400">Speaks: {guide.languages}</p>
                <p className="font-bold text-emerald-800 text-sm pt-1">{guide.rate}</p>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* 9. Travel Communities */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-6">
        <div className="flex items-end justify-between">
          <div>
            <div className="inline-flex items-center space-x-2 text-xs font-bold text-purple-800 uppercase tracking-wider bg-purple-50 px-2.5 py-1 rounded-full mb-2">
              <Users className="w-3.5 h-3.5 text-purple-600" />
              <span>Connect with Clubs</span>
            </div>
            <h2 className="text-2xl sm:text-3xl font-extrabold text-slate-900 tracking-tight">
              Vibrant Travel Communities
            </h2>
          </div>
          <Link to="/groups" className="text-purple-700 hover:text-purple-800 text-sm font-semibold flex items-center space-x-1">
            <span>Browse all groups</span>
            <ChevronRight className="w-4 h-4" />
          </Link>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {[
            {
              name: 'Goa Solo Travelers Club',
              dest: 'Goa',
              members: '3 Active Members',
              desc: 'Meetups, cafe co-working, sunset volleyball, and scooter road trips in Goa.',
              image: 'https://images.unsplash.com/photo-1512343879784-a960bf40e7f2?w=600&auto=format&fit=crop&q=80'
            },
            {
              name: 'Himalayan Trekkers & Trailblazers',
              dest: 'Manali, Himachal',
              members: '1 Active Member',
              desc: 'Connecting hikers, mountain lovers, and outdoor photographers exploring Himachal.',
              image: 'https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=600&auto=format&fit=crop&q=80'
            },
            {
              name: 'Heritage Wanderers of India',
              dest: 'Jaipur, Rajasthan',
              members: '1 Active Member',
              desc: 'A collaborative circle for lovers of royal architecture, stepwells, and traditional crafts.',
              image: 'https://images.unsplash.com/photo-1524492412937-b28074a5d7da?w=600&auto=format&fit=crop&q=80'
            }
          ].map((group, idx) => (
            <div key={idx} className="bg-white rounded-2xl border border-slate-100 overflow-hidden shadow-sm hover:shadow-lg transition-shadow">
              <img src={group.image} alt={group.name} className="h-40 w-full object-cover" />
              <div className="p-5 space-y-2">
                <span className="text-[10px] font-bold uppercase tracking-wider text-purple-700 bg-purple-50 px-2 py-0.5 rounded-full">
                  {group.dest} • {group.members}
                </span>
                <h3 className="font-bold text-slate-900 text-base">{group.name}</h3>
                <p className="text-xs text-slate-500 leading-relaxed">{group.desc}</p>
                <Link
                  to="/groups"
                  className="block text-center w-full py-2 rounded-xl bg-slate-900 text-white font-semibold text-xs hover:bg-slate-800 transition-colors mt-3"
                >
                  Join Community
                </Link>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* 10. How WanderLink Works */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="text-center max-w-2xl mx-auto mb-12 space-y-2">
          <h2 className="text-3xl font-extrabold text-slate-900 tracking-tight">How WanderLink Works</h2>
          <p className="text-sm text-slate-500">From planning to companionship in four simple steps</p>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8">
          {[
            {
              step: '01',
              title: 'Create Your Trip',
              desc: 'Share where you are traveling, your dates, budget, interests and travel style.'
            },
            {
              step: '02',
              title: 'Get Ranked Matches',
              desc: 'Our algorithm scores compatibility from 0 to 100% across 5 core dimensions.'
            },
            {
              step: '03',
              title: 'Connect & Plan',
              desc: 'Send connection requests, chat, and join verified destination communities.'
            },
            {
              step: '04',
              title: 'Immerse Locally',
              desc: 'Book local family homestays, homemade meals, and local guide walking tours.'
            }
          ].map((item, idx) => (
            <div key={idx} className="relative p-6 rounded-2xl bg-white border border-slate-100 shadow-sm space-y-3">
              <span className="text-3xl font-black text-emerald-200 block">{item.step}</span>
              <h3 className="font-bold text-slate-900 text-lg">{item.title}</h3>
              <p className="text-xs text-slate-500 leading-relaxed">{item.desc}</p>
            </div>
          ))}
        </div>
      </section>

      {/* 11. Trust and Safety Information */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="rounded-3xl bg-slate-900 text-white p-8 lg:p-12 space-y-6">
          <div className="flex items-center space-x-3">
            <div className="p-2.5 rounded-xl bg-emerald-600/30 text-emerald-400">
              <ShieldCheck className="w-6 h-6" />
            </div>
            <div>
              <h3 className="text-xl font-bold tracking-tight">Trust, Safety & Algorithm Integrity</h3>
              <p className="text-xs text-slate-400">Our promise to travelers and host families</p>
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-6 text-xs text-slate-300 leading-relaxed">
            <div className="bg-slate-800/60 p-4 rounded-xl border border-slate-700/50 space-y-1.5">
              <h4 className="font-bold text-white text-sm">Preference Compatibility Only</h4>
              <p>Our match score reflects shared interests, dates and budget compatibility. It is never represented as a background or identity trust score.</p>
            </div>
            <div className="bg-slate-800/60 p-4 rounded-xl border border-slate-700/50 space-y-1.5">
              <h4 className="font-bold text-white text-sm">Empowering Local Economies</h4>
              <p>100% of accommodation and food inquiries connect you directly with local host families, keeping travel expenditures within host communities.</p>
            </div>
            <div className="bg-slate-800/60 p-4 rounded-xl border border-slate-700/50 space-y-1.5">
              <h4 className="font-bold text-white text-sm">AI Guidance with Human Care</h4>
              <p>Our AI Travel Assistant provides transparent suggestions with deterministic fallbacks. We never fabricate unverified provider availability.</p>
            </div>
          </div>
        </div>
      </section>

      {/* 12. Bottom CTA Banner */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="text-center rounded-3xl bg-gradient-to-tr from-emerald-800 to-teal-700 text-white p-10 lg:p-14 space-y-6 shadow-xl">
          <h2 className="text-3xl sm:text-4xl font-extrabold tracking-tight">
            Ready to Begin Your Next Adventure?
          </h2>
          <p className="text-slate-200 text-sm sm:text-base max-w-xl mx-auto">
            Join hundreds of solo travelers, homestay hosts, and local food artisans already connected on WanderLink.
          </p>
          <div className="flex flex-wrap justify-center gap-4 pt-2">
            <Link
              to="/register"
              className="px-6 py-3.5 rounded-xl bg-orange-600 hover:bg-orange-700 text-white font-bold text-sm shadow-lg transition-transform hover:-translate-y-0.5"
            >
              Create Free Account
            </Link>
            <Link
              to="/ai-assistant"
              className="px-6 py-3.5 rounded-xl bg-white/10 hover:bg-white/20 text-white font-semibold text-sm backdrop-blur-md border border-white/20 transition-colors"
            >
              Ask AI Travel Assistant
            </Link>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Home;
