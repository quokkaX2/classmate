// document.addEventListener("DOMContentLoaded", function() {
//     // Initialize the navbar on page load
//     updateHoriSelector();
//
//     // Event listener for window resize to adjust the hori-selector
//     window.addEventListener('resize', updateHoriSelector);
//
//     // Toggle navbar collapse on click for smaller screens
//     document.querySelector(".navbar-toggler").addEventListener("click", function() {
//         document.querySelector(".navbar-collapse").classList.toggle("show");
//         // Allow the navbar to adjust before recalculating the hori-selector position
//         setTimeout(updateHoriSelector, 100);
//     });
//
//     // Update the hori-selector and active class on navbar item click
//     document.querySelectorAll("#navbarSupportedContent li").forEach(function(item) {
//         item.addEventListener("click", function(e) {
//             // Remove 'active' class from all items and add to the current item
//             document.querySelectorAll('#navbarSupportedContent ul li').forEach(li => li.classList.remove("active"));
//             this.classList.add('active');
//             updateHoriSelector();
//         });
//     });
//
//     function updateHoriSelector() {
//         const activeItem = document.querySelector("#navbarSupportedContent .active");
//         if (!activeItem) return; // Exit if no active item found
//
//         const horiSelector = document.querySelector(".hori-selector");
//         const itemRect = activeItem.getBoundingClientRect();
//
//         horiSelector.style.top = `${activeItem.offsetTop}px`; // Align top based on offsetTop for a more stable positioning
//         horiSelector.style.left = `${activeItem.offsetLeft}px`;
//         horiSelector.style.height = `${activeItem.offsetHeight}px`; // Use offsetHeight for full item height, including padding and borders
//         horiSelector.style.width = `${itemRect.width}px`; // Keep width dynamic to adjust to resizing
//     }
//
//     // Highlight the current page's navbar link as active
//     highlightCurrentPage();
// });
//
// function highlightCurrentPage() {
//     // Use a more general method to determine the current page identifier
//     const path = window.location.pathname;
//     const links = document.querySelectorAll('#navbarSupportedContent ul li a');
//     links.forEach(link => {
//         // Modify the condition to better match your application's URL structure
//         if (path.endsWith(link.getAttribute('href'))) {
//             links.forEach(l => l.parentNode.classList.remove('active'));
//             link.parentNode.classList.add('active');
//         }
//     });
// }
// document.addEventListener("DOMContentLoaded", function() {
//     // MutationObserver to dynamically adjust the hori-selector based on changes
//     const config = { attributes: true, childList: true, subtree: true };
//     const callback = function(mutationsList, observer) {
//         for(const mutation of mutationsList) {
//             if (mutation.type === 'attributes' && mutation.attributeName === 'class') {
//                 updateHoriSelector();
//             }
//         }
//     };
//
//     const observer = new MutationObserver(callback);
//     observer.observe(document.querySelector("#navbarSupportedContent"), config);
//
//     // Initialize the navbar on page load
//     updateHoriSelector();
//
//     // Event listener for window resize to adjust the hori-selector
//     window.addEventListener('resize', updateHoriSelector);
//
//     // Toggle navbar collapse on click for smaller screens
//     document.querySelector(".navbar-toggler").addEventListener("click", function() {
//         document.querySelector(".navbar-collapse").classList.toggle("show");
//         setTimeout(updateHoriSelector, 100); // Recalculate after navbar adjustment
//     });
//
//     // Update the hori-selector and active class on navbar item click
//     document.querySelectorAll("#navbarSupportedContent li").forEach(function(item) {
//         item.addEventListener("click", function(e) {
//             document.querySelectorAll('#navbarSupportedContent ul li').forEach(li => li.classList.remove("active"));
//             this.classList.add('active');
//             updateHoriSelector();
//         });
//     });
//
//     // Highlight the current page's navbar link
//     highlightCurrentPage();
// });
//
// function updateHoriSelector() {
//     const activeItem = document.querySelector("#navbarSupportedContent .active");
//     if (!activeItem) return; // Exit if no active item found
//
//     const horiSelector = document.querySelector(".hori-selector");
//     const itemRect = activeItem.getBoundingClientRect();
//
//     horiSelector.style.top = `${activeItem.offsetTop}px`;
//     horiSelector.style.left = `${activeItem.offsetLeft}px`;
//     horiSelector.style.height = `${activeItem.offsetHeight}px`;
//     horiSelector.style.width = `${itemRect.width}px`;
// }
//
// function highlightCurrentPage() {
//     const path = window.location.pathname;
//     const links = document.querySelectorAll('#navbarSupportedContent ul li a');
//
//     links.forEach(l => l.parentNode.classList.remove('active'));
//
//     links.forEach(link => {
//         let href = link.getAttribute('href');
//         let fullUrl = window.location.origin + href;
//
//         if (path === href || window.location.href === fullUrl) {
//             link.parentNode.classList.add('active');
//         }
//     });
// }
const courses = document.querySelector("#courses");
const profile = document.querySelector("#profile");
const cart = document.querySelector("#cart");




if(courses){
    courses.addEventListener("click", () => {
        window.location.href = "/";
    });
}

if (profile) {
    profile.addEventListener("click", () => {
        window.location.href = "/";
    });
}
if (cart) {
    cart.addEventListener("click", () => {
        console.log("cart click")
        window.location.href = "/cart";
    });
}

