/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        brand: {
          dark: '#064E3B',    // Deep forest green
          primary: '#0F5132', // Deep green
          light: '#10B981',   // Soft green
          accent: '#EA580C',  // Warm sunset orange
          accentHover: '#C2410C',
          sand: '#FDFBF7',    // Light warm background
          surface: '#FFFFFF',
          muted: '#64748B',
        }
      },
      fontFamily: {
        sans: ['Inter', 'system-ui', '-apple-system', 'sans-serif'],
      }
    },
  },
  plugins: [],
}
