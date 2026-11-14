import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { ToastProvider } from './context/ToastContext';

// Components
import Navbar from './components/Navbar';
import Footer from './components/Footer';
import ProtectedRoute from './components/ProtectedRoute';

// Pages
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import CreateTrip from './pages/CreateTrip';
import BuddyMatches from './pages/BuddyMatches';
import Explore from './pages/Explore';
import Accommodation from './pages/Accommodation';
import FoodExperiences from './pages/FoodExperiences';
import LocalGuides from './pages/LocalGuides';
import TravelGroups from './pages/TravelGroups';
import AITravelAssistant from './pages/AITravelAssistant';
import Profile from './pages/Profile';
import TravelerProfile from './pages/TravelerProfile';

function App() {
  return (
    <Router>
      <AuthProvider>
        <ToastProvider>
          <div className="min-h-screen flex flex-col bg-slate-50 text-slate-800">
            <Navbar />
            <main className="flex-grow">
              <Routes>
                {/* Public Pages */}
                <Route path="/" element={<Home />} />
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/explore" element={<Explore />} />
                <Route path="/accommodations" element={<Accommodation />} />
                <Route path="/food" element={<FoodExperiences />} />
                <Route path="/guides" element={<LocalGuides />} />
                <Route path="/groups" element={<TravelGroups />} />
                <Route path="/ai-assistant" element={<AITravelAssistant />} />
                <Route path="/matches" element={<BuddyMatches />} />
                <Route path="/travelers/:userId" element={<TravelerProfile />} />

                {/* Protected Pages */}
                <Route
                  path="/dashboard"
                  element={
                    <ProtectedRoute>
                      <Dashboard />
                    </ProtectedRoute>
                  }
                />
                <Route
                  path="/create-trip"
                  element={
                    <ProtectedRoute>
                      <CreateTrip />
                    </ProtectedRoute>
                  }
                />
                <Route
                  path="/profile"
                  element={
                    <ProtectedRoute>
                      <Profile />
                    </ProtectedRoute>
                  }
                />
              </Routes>
            </main>
            <Footer />
          </div>
        </ToastProvider>
      </AuthProvider>
    </Router>
  );
}

export default App;
