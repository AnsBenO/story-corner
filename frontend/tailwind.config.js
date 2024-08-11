/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{html,ts}"],
  theme: {
    extend: {
      colors: {
        aquamarine: {
          light: "#a6ffecff",
          DEFAULT: "#62f9c9ff",
          dark: "#2cd7a6ff",
        },
        "maya-blue": {
          light: "#b0e0ffff",
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
