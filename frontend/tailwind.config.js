/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{html,ts}"],
  theme: {
    fontFamily: {
      Merriweather: ['"Merriweather Sans"', "sans-serif"],
      Suse: ['"SUSE"', "sans-serif"],
      Montserrat: ["Montserrat", "sans-serif"],
    },
    extend: {
      colors: {
        celeste: {
          light: "#B5FFFB",
          DEFAULT: "#7FFFEF",
          dark: "#66CCC0",
        },
        "maya-blue": {
          light: "#C2E0F4",
          DEFAULT: "#80c5faff",
          dark: "#4aa9d9ff",
        },
        "alice-blue": {
          light: "#fbfcffff",
          DEFAULT: "#f5faffff",
          dark: "#eef6ffff",
        },
        "cool-gray": {
          light: "#b0b3c1ff",
          DEFAULT: "#888abeff",
          dark: "#5f5f7aff",
        },
        night: {
          light: "#343536ff",
          DEFAULT: "#090a0bff",
          dark: "#050506ff",
        },
      },
    },
    plugins: [],
  },
};
