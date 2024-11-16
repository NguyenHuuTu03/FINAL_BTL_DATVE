// const apiUrl = 'http://localhost:8080/api/movies/all';
// let movieList = [];
//
// // Hàm hiển thị danh sách phim trong bảng
// function renderMovieTable(movies) {
//     const movieTable = document.getElementById('movieTable');
//     movieTable.innerHTML = ''; // Xóa dữ liệu cũ trong bảng
//
//     movies.forEach(movie => {
//         const row = document.createElement('tr');
//         row.innerHTML = `
//                 <td>${movie.movieId}</td>
//                 <td>${movie.movieName}</td>
//                 <td>${movie.movieDirector}</td>
//                 <td>${movie.movieActor}</td>
//                 <td>${movie.movieDuration} phút</td>
//                 <td>${movie.movieViews}</td>
//                 <td>
//                     <button onclick="editMovie(${movie.movieId})">Sửa</button>
//                     <button onclick="deleteMovie(${movie.movieId})">Xóa</button>
//                 </td>
//             `;
//         movieTable.appendChild(row);
//     });
// }
//
// // Function to fetch movies from the API
// async function fetchMovies() {
//     try {
//         const response = await fetch(apiUrl);
//         if (!response.ok) throw new Error("Không thể tải dữ liệu phim");
//         movieList = await response.json();
//         renderMovieTable(movieList);
//     } catch (error) {
//         alert(error.message);
//     }
// }
//
// // Call the fetchMovies function when the page loads
// document.addEventListener('DOMContentLoaded', () => {
//     fetchMovies();
// });
//
// const searchApi = 'http://localhost:8080/api/movies/search';
//
// // Hàm tìm kiếm phim theo tên qua API
//
// async function searchMovies() {
//     const searchInput = document.getElementById('searchInput').value;
//     const response = await fetch(`http://localhost:8080/api/movies?search=${searchInput}`);
//     const movies = await response.json();
//     const movieTable = document.getElementById('movieTable');
//     movieTable.innerHTML = '';
//     movies.forEach(movie => {
//         movieTable.innerHTML += `
//                     <tr>
//                         <td>${movie.id}</td>
//                         <td>${movie.name}</td>
//                         <td>${movie.director}</td>
//                         <td>${movie.actors}</td>
//                         <td>${movie.duration}</td>
//                         <td>${movie.viewer}</td>
//                         <td>
//                             <button class="btn btn-primary" onclick="editMovie(${movie.id})">Sửa</button>
//                             <button class="btn btn-danger" onclick="deleteMovie(${movie.id})">Xóa</button>
//                         </td>
//                     </tr>
//                 `;
//     });
// }
//
// // Lọc phim theo các tiêu chí
// function filterMovies(filterType) {
//     let filteredMovies = [];
//     switch (filterType) {
//         case 'new':
//             // Lọc phim mới (giả sử phim có lượt xem thấp hơn 1000 là phim mới)
//             filteredMovies = movieList.filter(movie => movie.views < 1000);
//             break;
//         case 'mostViewed':
//             filteredMovies = [...movieList].sort((a, b) => b.views - a.views);
//             break;
//         case 'leastViewed':
//             filteredMovies = [...movieList].sort((a, b) => a.views - b.views);
//             break;
//         case 'all':
//             filteredMovies = movieList;
//             break;
//     }
//     renderMovieTable(filteredMovies);
// }
// async function confirmAddOrUpdateMovie() {
//     const movieId = document.getElementById('movieId').value;
//     const movieName = document.getElementById('movieName').value;
//     const director = document.getElementById('director').value;
//     const actors = document.getElementById('actors').value;
//     const duration = document.getElementById('duration').value;
//
//     const movie = { name: movieName, director, actors, duration };
//
//     const requestOptions = {
//         method: movieId ? 'PUT' : 'POST',
//         headers: { 'Content-Type': 'application/json' },
//         body: JSON.stringify(movie)
//     };
//
//     const response = await fetch(movieId ? `${apiURL}/${movieId}` : apiURL, requestOptions);
//     if (response.ok) {
//         alert(movieId ? 'Cập nhật thành công!' : 'Thêm thành công!');
//         fetchMovies();
//         document.getElementById('movieForm').reset();
//     } else {
//         alert('Lỗi khi lưu phim');
//     }
// }
//
// async function editMovie(movieId) {
//     const response = await fetch(`${apiURL}/${movieId}`);
//     const movie = await response.json();
//
//     document.getElementById('movieId').value = movie.movieId;
//     document.getElementById('movieName').value = movie.movieName;
//     document.getElementById('director').value = movie.movieDirector;
//     document.getElementById('actors').value = movie.movieActor;
//     document.getElementById('duration').value = movie.movieDuration;
// }
//
// apiUrl1 = 'http://localhost:8080/api/movies';
// async function deleteMovie(movieId) {
//     if (confirm('Bạn có chắc chắn muốn xóa phim này?')) {
//         const response = await fetch(`${apiUrl1}/${movieId}`, { method: 'DELETE' });
//         if (response.ok) {
//             alert('Xóa thành công!');
//             fetchMovies();
//         } else {
//             alert('Lỗi khi xóa phim');
//         }
//     }
// }
// document.addEventListener('DOMContentLoaded', fetchMovies);
//
// function toggleFilterOptions() {
//     const dropdown = document.getElementById('filterOptionsDropdown');
//     dropdown.style.display = dropdown.style.display === 'block' ? 'none' : 'block';
// }
