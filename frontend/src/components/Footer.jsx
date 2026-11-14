import React from 'react';
import { Link } from 'react-router-dom';
import { Globe, Heart, Mail, Github, Twitter, Instagram } from 'lucide-react';

const Footer = () => {
  const currentYear = new Date().getFullYear();

  return (
    <footer className="bg-slate-900 text-slate-300">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
          {/* Brand */}
          <div className="col-span-1 md:col-span-1">
            <div className="flex items-center gap-2 mb-4">
              <Globe className="w-7 h-7 text-emerald-400" />
              <span className="text-xl font-bold text-white">WanderLink</span>
            </div>
            <p className="text-sm text-slate-400 leading-relaxed mb-4">
              Every Journey Starts With a Connection. Discover compatible travel
              buddies, experience local culture, and connect with communities
              wherever your journey takes you.
            </p>
            <div className="flex gap-3">
              <a
                href="#"
                className="w-8 h-8 rounded-full bg-slate-700 hover:bg-emerald-600 flex items-center justify-center transition-colors"
                aria-label="Twitter"
              >
                <Twitter className="w-4 h-4" />
              </a>
              <a
                href="#"
                className="w-8 h-8 rounded-full bg-slate-700 hover:bg-emerald-600 flex items-center justify-center transition-colors"
                aria-label="Instagram"
              >
                <Instagram className="w-4 h-4" />
              </a>
              <a
                href="#"
                className="w-8 h-8 rounded-full bg-slate-700 hover:bg-emerald-600 flex items-center justify-center transition-colors"
                aria-label="GitHub"
              >
                <Github className="w-4 h-4" />
              </a>
            </div>
          </div>

          {/* Explore */}
          <div>
            <h3 className="text-white font-semibold mb-4">Explore</h3>
            <ul className="space-y-2 text-sm">
              <li>
                <Link to="/explore" className="hover:text-emerald-400 transition-colors">
                  Discover
                </Link>
              </li>
              <li>
                <Link to="/accommodations" className="hover:text-emerald-400 transition-colors">
                  Accommodation
                </Link>
              </li>
              <li>
                <Link to="/food" className="hover:text-emerald-400 transition-colors">
                  Food Experiences
                </Link>
              </li>
              <li>
                <Link to="/guides" className="hover:text-emerald-400 transition-colors">
                  Local Guides
                </Link>
              </li>
              <li>
                <Link to="/groups" className="hover:text-emerald-400 transition-colors">
                  Travel Groups
                </Link>
              </li>
            </ul>
          </div>

          {/* Travelers */}
          <div>
            <h3 className="text-white font-semibold mb-4">For Travelers</h3>
            <ul className="space-y-2 text-sm">
              <li>
                <Link to="/register" className="hover:text-emerald-400 transition-colors">
                  Create Account
                </Link>
              </li>
              <li>
                <Link to="/dashboard" className="hover:text-emerald-400 transition-colors">
                  My Dashboard
                </Link>
              </li>
              <li>
                <Link to="/create-trip" className="hover:text-emerald-400 transition-colors">
                  Create a Trip
                </Link>
              </li>
              <li>
                <Link to="/matches" className="hover:text-emerald-400 transition-colors">
                  Find Buddy Matches
                </Link>
              </li>
              <li>
                <Link to="/ai-assistant" className="hover:text-emerald-400 transition-colors">
                  AI Travel Assistant
                </Link>
              </li>
            </ul>
          </div>

          {/* Contact */}
          <div>
            <h3 className="text-white font-semibold mb-4">Contact</h3>
            <ul className="space-y-2 text-sm">
              <li className="flex items-center gap-2 text-slate-400">
                <Mail className="w-4 h-4 text-emerald-400 flex-shrink-0" />
                <span>hello@wanderlink.app</span>
              </li>
              <li className="flex items-center gap-2 text-slate-400">
                <Globe className="w-4 h-4 text-emerald-400 flex-shrink-0" />
                <span>wanderlink.app</span>
              </li>
            </ul>

            <div className="mt-6">
              <h4 className="text-white font-semibold mb-2 text-sm">Trust & Safety</h4>
              <p className="text-xs text-slate-500 leading-relaxed">
                WanderLink match scores reflect travel preference compatibility only.
                Always verify identity independently before meeting travel companions.
                AI itineraries are suggestions, not guaranteed services.
              </p>
            </div>
          </div>
        </div>

        {/* Bottom bar */}
        <div className="border-t border-slate-800 mt-10 pt-6 flex flex-col sm:flex-row items-center justify-between gap-3">
          <p className="text-sm text-slate-500">
            © {currentYear} WanderLink. Built with{' '}
            <Heart className="w-3 h-3 inline text-red-400" /> for the community.
          </p>
          <div className="flex gap-4 text-xs text-slate-500">
            <a href="#" className="hover:text-slate-300 transition-colors">Privacy Policy</a>
            <a href="#" className="hover:text-slate-300 transition-colors">Terms of Service</a>
            <a href="#" className="hover:text-slate-300 transition-colors">Safety Guidelines</a>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
