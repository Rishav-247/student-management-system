/**
 * Student Management System - Swiss Brutalist Minimal JavaScript
 */

// Immediate Theme Setup to avoid FOUC
(function() {
    const savedTheme = localStorage.getItem('sms_theme');
    if (savedTheme === 'dark' || (!savedTheme && window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
        document.documentElement.setAttribute('data-theme', 'dark');
    } else {
        document.documentElement.setAttribute('data-theme', 'light');
    }
})();

document.addEventListener('DOMContentLoaded', () => {
    // Theme Toggle Handler
    const themeBtn = document.getElementById('theme-toggle-btn');
    const themeIcon = document.getElementById('theme-icon');
    const themeText = document.getElementById('theme-text');

    function updateThemeUI(theme) {
        if (themeIcon && themeText) {
            if (theme === 'dark') {
                themeIcon.textContent = '☀️';
                themeText.textContent = 'LIGHT';
            } else {
                themeIcon.textContent = '🌙';
                themeText.textContent = 'DARK';
            }
        }
    }

    const currentTheme = document.documentElement.getAttribute('data-theme') || 'light';
    updateThemeUI(currentTheme);

    if (themeBtn) {
        themeBtn.addEventListener('click', () => {
            const activeTheme = document.documentElement.getAttribute('data-theme');
            const newTheme = activeTheme === 'dark' ? 'light' : 'dark';
            document.documentElement.setAttribute('data-theme', newTheme);
            localStorage.setItem('sms_theme', newTheme);
            updateThemeUI(newTheme);
        });
    }

    // Mobile sidebar toggle
    const toggleBtn = document.getElementById('sidebar-toggle');
    const sidebar = document.querySelector('.sidebar');

    if (toggleBtn && sidebar) {
        toggleBtn.addEventListener('click', () => {
            sidebar.classList.toggle('open');
        });
    }

    // Auto-dismiss alerts or manual close
    const alerts = document.querySelectorAll('.alert-brutal');
    alerts.forEach(alert => {
        const closeBtn = alert.querySelector('.alert-close');
        if (closeBtn) {
            closeBtn.addEventListener('click', () => {
                alert.style.display = 'none';
            });
        }
    });

    // Confirmation dialog for deletes
    const deleteForms = document.querySelectorAll('form.delete-form');
    deleteForms.forEach(form => {
        form.addEventListener('submit', (e) => {
            const confirmed = confirm('CONFIRM ACTION: ARE YOU SURE YOU WANT TO DELETE THIS RECORD?');
            if (!confirmed) {
                e.preventDefault();
            }
        });
    });
});

