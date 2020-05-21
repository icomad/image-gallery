const plugin = require('tailwindcss/plugin');

module.exports = {
    theme: {
        extend: {
            fontFamily: {
                inter: ['Inter', 'sans-serif']
            },
            inset: {
                '1/2': '50%',
                '1/4': '25%',
                '3/4': '75%',
                full: '100%',
                '-1/2': '-50%',
                '-1/4': '-25%',
                '-3/4': '-75%',
                '-full': '-100%',
                '-fullest': '-150%'
            },
            maxHeight: {
                '0': '0',
                '1/4': '25%',
                '1/2': '50%',
                '3/4': '75%',
                screen: '100vh'
            },
            maxWidth: {
                '0': '0',
                '1/4': '25%',
                '1/2': '50%',
                '3/4': '75%',
                screen: '100vw'
            }
        }
    },
    variants: {
        backgroundColor: ['responsive', 'hover', 'focus', 'active', 'disabled'],
        opacity: ['responsive', 'hover', 'focus', 'active', 'disabled'],
        cursor: ['hover', 'disabled'],
        display: ['responsive', 'group-hover']
    },
    plugins: []
};