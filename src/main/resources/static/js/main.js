// add hovered class to selected list item
let list = document.querySelectorAll(".navigation li");

function activeLink() {
  list.forEach((item) => {
    item.classList.remove("hovered");
  });
  this.classList.add("hovered");
}

list.forEach((item) => item.addEventListener("mouseover", activeLink));

// Menu Toggle
let toggle = document.querySelector(".toggle");
let navigation = document.querySelector(".navigation");
let main = document.querySelector(".main");

toggle.onclick = function () {
  navigation.classList.toggle("active");
  main.classList.toggle("active");
};

const apiUrl = 'http://localhost:8080/api/movies/all';
let movies = [];

function displayMovies(movies) {
  const table = document.getElementById("movieTable");
  table.innerHTML = ""; // Clear old content

  movies.forEach(movie => {
    const row = document.createElement("tr");
    row.innerHTML = `
            <td>${movie.movieId}</td>
            <td>${movie.movieName}</td>
            <td>${movie.movieDirector}</td>
            <td>${movie.movieActor}</td>
            <td>${movie.movie_genreList}</td>
            <td>${movie.moviePoster}</td>
            <td>${movie.movieReleaseDate}</td>
            <td>${movie.movieDuration} phút</td>
            <td>${movie.movieViews}</td>
            <td>
                <button class="btn-sua" onclick="editMovie(${movie.movieId})">Sửa</button>
                <button class="btn-xoa" onclick="confirmDeleteMovie(${movie.movieId})">Xóa</button>
            </td>
        `;
    table.appendChild(row);
  });
}

// Function to fetch movies from the API
async function fetchMovies() {
  try {
    const response = await fetch(apiUrl);
    if (!response.ok) throw new Error("Không thể tải dữ liệu phim");

    const movies = await response.json(); // Parse JSON response
    return movies; // Return movies data
  } catch (error) {
    console.log(error);
    alert("Đã xảy ra lỗi khi tải dữ liệu phim.");
    return []; // Return an empty array if there is an error
  }
}

// Call the fetchMovies function when the page loads
document.addEventListener('DOMContentLoaded', async () => {
  const movies = await fetchMovies();
  displayMovies(movies);
});

// Search Movies
function searchMovies() {
  const searchInput = document.getElementById("searchInput").value.toLowerCase();
  const filteredMovies = movies.filter(movie =>
      movie.movieName.toLowerCase().includes(searchInput)
  );
  displayMovies(filteredMovies);
}

// Filter Movies
function filterMovies(filterType) {
  let filteredMovies = [];

  switch (filterType) {
    case 'new':
      filteredMovies = movies.filter(movie => movie.movieViews < 10);
      break;
    case 'mostViewed':
      filteredMovies = movies.filter(movie => movie.movieViews >= 50);
      break;
    case 'leastViewed':
      filteredMovies = movies.filter(movie => movie.movieViews < 50);
      break;
    case 'all':
      filteredMovies = movies; // Show all movies
      break;
    default:
      filteredMovies = movies;
  }
  displayMovies(filteredMovies);
}

// Add or Update Movie
async function confirmAddOrUpdateMovie() {
  const movieId = document.getElementById("movieId").value;
  const movieName = document.getElementById("movieName").value;
  const movieDirector = document.getElementById("director").value;
  const movieActor = document.getElementById("actors").value;
  const movieDuration = document.getElementById("duration").value;
  const movieViews = document.getElementById("viewer").value;

  const movie = {
    movieId: movieId || null,
    movieName: movieName,
    movieDirector: movieDirector,
    movieActor: movieActor,
    movieDuration: parseInt(movieDuration),
    movieViews: parseInt(movieViews)
  };

  try {
    const response = await fetch(apiUrl + (movieId ? `/${movieId}` : ''), {
      method: movieId ? 'PUT' : 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(movie)
    });
    if (!response.ok) throw new Error("Lỗi khi thêm hoặc cập nhật phim");

    fetchMovies(); // Reload the movie list
    alert("Thành công!");
  } catch (error) {
    alert(error.message);
  }
}

// Edit Movie (populate form)
function editMovie(movieId) {
  const movie = movies.find(m => m.movieId === movieId);
  if (movie) {
    document.getElementById("movieId").value = movie.movieId;
    document.getElementById("movieName").value = movie.movieName;
    document.getElementById("director").value = movie.movieDirector;
    document.getElementById("actors").value = movie.movieActor;
    document.getElementById("duration").value = movie.movieDuration;
    document.getElementById("viewer").value = movie.movieViews;
  }
}

// Delete Movie
async function confirmDeleteMovie(movieId) {
  if (confirm("Bạn có chắc chắn muốn xóa phim này?")) {
    try {
      const response = await fetch(apiUrl + `/${movieId}`, {
        method: 'DELETE'
      });
      if (!response.ok) throw new Error("Lỗi khi xóa phim");

      fetchMovies(); // Reload the movie list
      alert("Xóa thành công!");
    } catch (error) {
      alert(error.message);
    }
  }
}

// Load movies when the page is ready
document.addEventListener('DOMContentLoaded', fetchMovies);


