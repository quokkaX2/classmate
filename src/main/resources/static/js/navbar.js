document.addEventListener("DOMContentLoaded", function() {
    // Initialize the navbar on page load
    updateHoriSelector();

    // Event listener for window resize to adjust the hori-selector
    window.addEventListener('resize', updateHoriSelector);

    // Toggle navbar collapse on click for smaller screens
    document.querySelector(".navbar-toggler").addEventListener("click", function() {
        document.querySelector(".navbar-collapse").classList.toggle("show");
        // Allow the navbar to adjust before recalculating the hori-selector position
        setTimeout(updateHoriSelector, 100);
    });

    // Update the hori-selector and active class on navbar item click
    document.querySelectorAll("#navbarSupportedContent li").forEach(function(item) {
        item.addEventListener("click", function(e) {
            // Remove 'active' class from all items and add to the current item
            document.querySelectorAll('#navbarSupportedContent ul li').forEach(li => li.classList.remove("active"));
            this.classList.add('active');
            updateHoriSelector();
        });
    });

    function updateHoriSelector() {
        const activeItem = document.querySelector("#navbarSupportedContent .active");
        if (!activeItem) return; // Exit if no active item found

        const horiSelector = document.querySelector(".hori-selector");
        const itemRect = activeItem.getBoundingClientRect();

        horiSelector.style.top = `${activeItem.offsetTop}px`; // Align top based on offsetTop for a more stable positioning
        horiSelector.style.left = `${activeItem.offsetLeft}px`;
        horiSelector.style.height = `${activeItem.offsetHeight}px`; // Use offsetHeight for full item height, including padding and borders
        horiSelector.style.width = `${itemRect.width}px`; // Keep width dynamic to adjust to resizing
    }

    // Highlight the current page's navbar link as active
    highlightCurrentPage();
});

function highlightCurrentPage() {
    // Use a more general method to determine the current page identifier
    const path = window.location.pathname;
    const links = document.querySelectorAll('#navbarSupportedContent ul li a');
    links.forEach(link => {
        // Modify the condition to better match your application's URL structure
        if (path.endsWith(link.getAttribute('href'))) {
            links.forEach(l => l.parentNode.classList.remove('active'));
            link.parentNode.classList.add('active');
        }
    });
}
