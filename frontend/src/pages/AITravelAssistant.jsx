import React, { useState, useRef, useEffect } from 'react';
import { aiService } from '../services/aiService';
import { useToast } from '../context/ToastContext';
import { 
  Sparkles, Send, Bot, User, MapPin, Calendar, 
  IndianRupee, Compass, CheckCircle2, AlertCircle, Loader2, RefreshCw 
} from 'lucide-react';

const AITravelAssistant = () => {
  const [messages, setMessages] = useState([
    {
      id: 1,
      sender: 'assistant',
      text: "Hello! I am your WanderLink AI Travel Assistant. I can curate personalized itineraries, recommend authentic local food, find hidden spots, and suggest ways to connect with fellow travelers. Where are you dreaming of going?",
      suggestions: [
        "Plan a 5-day budget trip to Goa",
        "Suggest budget-friendly destinations in India",
        "Find local food experiences in Goa",
        "Suggest activities for solo travelers",
        "Create a ₹12,000 itinerary for Manali"
      ],
      itinerary: null,
      destination: null,
    }
  ]);

  const [inputMessage, setInputMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const messagesEndRef = useRef(null);
  const { showToast } = useToast();

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages, loading]);

  const handleSendMessage = async (textToSend) => {
    const query = (textToSend || inputMessage).trim();
    if (!query) return;

    const userMessage = {
      id: Date.now(),
      sender: 'user',
      text: query,
    };

    setMessages((prev) => [...prev, userMessage]);
    setInputMessage('');
    setLoading(true);

    try {
      const history = messages
        .filter((message) => message.sender === 'user' || message.sender === 'assistant')
        .slice(-12)
        .map((message) => ({
          role: message.sender,
          content: message.text,
        }));
      const response = await aiService.askAssistant({
        message: query,
        history,
      });

      const assistantMessage = {
        id: Date.now() + 1,
        sender: 'assistant',
        text: response.reply,
        destination: response.destination,
        suggestions: response.suggestions,
        itinerary: response.itinerary,
        estimatedBudget: response.estimatedBudget,
      };

      setMessages((prev) => [...prev, assistantMessage]);
    } catch (err) {
      const errorMessage = err.message || 'Error communicating with AI assistant';
      showToast(errorMessage, 'error');
      setMessages((prev) => [
        ...prev,
        {
          id: Date.now() + 1,
          sender: 'assistant',
          text: errorMessage,
        }
      ]);
    } finally {
      setLoading(false);
    }
  };

  const promptSuggestions = [
    "Plan a five-day Goa trip",
    "Suggest budget-friendly destinations",
    "Find local food experiences",
    "Suggest activities for solo travelers",
    "Create an itinerary based on my budget",
  ];

  return (
    <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-6">
      {/* Header Banner */}
      <div className="bg-gradient-to-r from-orange-600 via-amber-600 to-emerald-700 rounded-3xl p-6 sm:p-8 text-white shadow-xl flex items-center justify-between">
        <div className="space-y-1">
          <div className="inline-flex items-center space-x-2 text-xs font-bold uppercase tracking-wider bg-white/20 px-3 py-1 rounded-full backdrop-blur-md">
            <Sparkles className="w-3.5 h-3.5 text-amber-200" />
            <span>AI Itinerary & Community Guide</span>
          </div>
          <h1 className="text-2xl sm:text-3xl font-extrabold tracking-tight">
            WanderLink Travel Assistant
          </h1>
          <p className="text-xs sm:text-sm text-orange-100">
            Intelligent itinerary planning connected with local homestays, homemade meals and travel buddies.
          </p>
        </div>
        <div className="hidden sm:block">
          <div className="w-16 h-16 rounded-2xl bg-white/10 backdrop-blur-md flex items-center justify-center border border-white/20">
            <Bot className="w-9 h-9 text-amber-200" />
          </div>
        </div>
      </div>

      {/* Suggested Quick Prompt Pills */}
      <div className="space-y-2">
        <span className="text-xs font-bold text-slate-400 uppercase tracking-wider">
          Suggested Prompts:
        </span>
        <div className="flex flex-wrap gap-2">
          {promptSuggestions.map((prompt, i) => (
            <button
              key={i}
              onClick={() => handleSendMessage(prompt)}
              className="px-3.5 py-1.5 rounded-full text-xs font-semibold bg-white text-slate-700 border border-slate-200 hover:border-orange-500 hover:text-orange-600 transition-colors shadow-sm flex items-center space-x-1.5"
            >
              <Sparkles className="w-3 h-3 text-orange-500" />
              <span>{prompt}</span>
            </button>
          ))}
        </div>
      </div>

      {/* Chat Messages Container */}
      <div className="bg-white rounded-3xl border border-slate-100 shadow-sm p-4 sm:p-6 min-h-[480px] max-h-[600px] overflow-y-auto space-y-5">
        {messages.map((msg) => (
          <div
            key={msg.id}
            className={`flex items-start space-x-3 ${
              msg.sender === 'user' ? 'flex-row-reverse space-x-reverse' : ''
            }`}
          >
            {/* Avatar */}
            <div
              className={`w-9 h-9 rounded-2xl flex items-center justify-center flex-shrink-0 text-white font-bold text-xs ${
                msg.sender === 'user' ? 'bg-slate-800' : 'bg-gradient-to-tr from-orange-600 to-amber-500'
              }`}
            >
              {msg.sender === 'user' ? <User className="w-5 h-5" /> : <Bot className="w-5 h-5" />}
            </div>

            {/* Bubble */}
            <div
              className={`max-w-2xl rounded-2xl p-4 sm:p-5 text-xs sm:text-sm leading-relaxed space-y-3 ${
                msg.sender === 'user'
                  ? 'bg-slate-900 text-white rounded-tr-none'
                  : 'bg-slate-50 text-slate-800 border border-slate-100 rounded-tl-none shadow-sm'
              }`}
            >
              <p className="whitespace-pre-line">{msg.text}</p>

              {/* Structured Itinerary Steps */}
              {msg.itinerary && msg.itinerary.length > 0 && (
                <div className="mt-3 pt-3 border-t border-slate-200/80 space-y-2">
                  <span className="font-bold text-slate-900 block text-xs uppercase tracking-wider">
                    📋 Day-by-Day Itinerary Plan:
                  </span>
                  <div className="space-y-1.5">
                    {msg.itinerary.map((step, idx) => (
                      <div key={idx} className="p-2.5 rounded-xl bg-white border border-slate-200/60 text-xs">
                        {step}
                      </div>
                    ))}
                  </div>
                </div>
              )}

              {/* Suggestions bullets */}
              {msg.suggestions && msg.suggestions.length > 0 && (
                <div className="mt-3 pt-3 border-t border-slate-200/80 space-y-1.5">
                  <span className="font-bold text-slate-900 block text-xs uppercase tracking-wider">
                    💡 Community Recommendations:
                  </span>
                  <ul className="space-y-1 text-xs">
                    {msg.suggestions.map((sug, idx) => (
                      <li key={idx} className="flex items-start space-x-1.5">
                        <CheckCircle2 className="w-3.5 h-3.5 text-emerald-600 flex-shrink-0 mt-0.5" />
                        <span>{sug}</span>
                      </li>
                    ))}
                  </ul>
                </div>
              )}
            </div>
          </div>
        ))}

        {loading && (
          <div className="flex items-start space-x-3">
            <div className="w-9 h-9 rounded-2xl bg-gradient-to-tr from-orange-600 to-amber-500 text-white flex items-center justify-center flex-shrink-0">
              <Bot className="w-5 h-5 animate-pulse" />
            </div>
            <div className="bg-slate-50 rounded-2xl p-4 border border-slate-100 rounded-tl-none flex items-center space-x-2 text-xs text-slate-500">
              <Loader2 className="w-4 h-4 animate-spin text-orange-600" />
              <span>Thinking & curating local suggestions...</span>
            </div>
          </div>
        )}

        <div ref={messagesEndRef} />
      </div>

      {/* Message Input Box */}
      <form
        onSubmit={(e) => {
          e.preventDefault();
          handleSendMessage();
        }}
        className="bg-white rounded-2xl p-2 border border-slate-200 shadow-lg flex items-center space-x-2"
      >
        <input
          type="text"
          value={inputMessage}
          onChange={(e) => setInputMessage(e.target.value)}
          placeholder="Ask anything... e.g. 'Plan a 5-day budget trip to Goa' or 'Where to find local thalis?'"
          className="w-full px-4 py-2.5 text-sm bg-transparent focus:outline-none placeholder:text-slate-400 font-medium"
        />
        <button
          type="submit"
          disabled={loading || !inputMessage.trim()}
          className="px-5 py-2.5 rounded-xl bg-orange-600 hover:bg-orange-700 text-white font-bold text-xs transition-colors shadow-md flex items-center space-x-1.5 disabled:opacity-40 flex-shrink-0"
        >
          <Send className="w-3.5 h-3.5" />
          <span>Ask</span>
        </button>
      </form>
    </div>
  );
};

export default AITravelAssistant;
