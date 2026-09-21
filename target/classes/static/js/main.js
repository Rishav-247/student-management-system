/**
 * Student Management System - Swiss Brutalist Minimal JavaScript
 */

document.addEventListener('DOMContentLoaded', () => {
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
