
    async function countUserAccounts(){
        try {
            const response = await fetch('http://localhost:8080/api/statistics/total-user');
            if (!response.ok) {
                throw new Error('Failed to fetch user accounts');
            }
            const responseText = await response.text();
            console.log("Response text:", responseText);
            const totalUser = JSON.parse(responseText);
            document.getElementById('totalUser').textContent = totalUser;
        } catch (err) {
            console.error(err);
        }
    }

    window.onload = countUserAccounts();

    async function countMovie(){
        try {
            const response = await fetch('http://localhost:8080/api/statistics/total-movie');
            if (!response.ok) {
                throw new Error('Failed to fetch movie');
            }

            const totalMovie = await response.json();

            document.getElementById('totalMovie').textContent = totalMovie;
        }catch (err) {
            console.error(err);
        }
    }

    window.onload = countMovie();

    async function countTicket(){
        try {
            const response = await fetch('http://localhost:8080/api/statistics/total-ticket-sold');
            if (!response.ok) {
                throw new Error('Failed to fetch ticket');
            }

            const totalTicket = await response.json();

            document.getElementById('totalTicket').textContent = totalTicket;
        }catch (err) {
            console.error(err);
        }
    }

    window.onload = countTicket();

    async function totalRevenue(){
        try {
            const response = await fetch('http://localhost:8080/api/statistics/total-revenue');
            if (!response.ok) {
                throw new Error('Failed to fetch ticket');
            }

            const totalRevenue = await response.json();

            document.getElementById('totalRevenue').textContent = totalRevenue;
        }catch (err) {
            console.error(err);
        }
    }

    window.onload = totalRevenue();

    async function getTopMovies() {
        try {
            const response = await fetch('http://localhost:8080/api/statistics/top-movie');
            const movies = await response.json();

            // Hiển thị thông tin phim trên bảng
            let table = document.getElementById("movieTableHot");
            table.innerHTML = ""; // Xóa nội dung cũ

            movies.forEach(movie => {
                let row = table.insertRow();
                let nameCell = row.insertCell(0);
                let posterCell = row.insertCell(1);
                let genreCell = row.insertCell(2);
                let durationCell = row.insertCell(3);
                let viewsCell = row.insertCell(4);

                nameCell.textContent = movie.name;

                let posterImg = document.createElement('img');
                posterImg.src = movie.poster;
                posterImg.alt = movie.name;
                posterImg.style.width = '100px'; // Set the desired width
                posterImg.style.height = 'auto'; // Maintain aspect ratio
                posterCell.textContent = posterImg;

                genreCell.textContent = movie.genres.join(', ');
                durationCell.textContent = movie.duration + " min";
                viewsCell.textContent = movie.views;
            });
        } catch (error) {
            console.error("Error fetching top movies:", error);
        }
    }

    getTopMovies();

    // Call the function when the page loads
    window.onload = getTopMovies;

// Function to open the modal
function openModal() {
    document.getElementById('editModal').style.display = 'block';
}

// Function to close the modal
function closeModal() {
    document.getElementById('editModal').style.display = 'none';
}

// Function to handle the edit button click
async function editMovie(movieId) {
    const response = await fetch(`${apiUrl1}/${movieId}`);
    const movie = await response.json();

    document.getElementById('editMovieId').value = movie.movieId;
    document.getElementById('editMovieName').value = movie.movieName;
    document.getElementById('editDirector').value = movie.movieDirector;
    document.getElementById('editActors').value = movie.movieActor;
    document.getElementById('editDuration').value = movie.movieDuration;

    openModal();
}

// Function to handle the form submission
async function confirmEditMovie() {
    const movieId = document.getElementById('editMovieId').value;
    const movie = {
        movieName: document.getElementById('editMovieName').value,
        movieDirector: document.getElementById('editDirector').value,
        movieActor: document.getElementById('editActors').value,
        movieDuration: document.getElementById('editDuration').value
    };

    const response = await fetch(`${apiUrl1}/${movieId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(movie)
    });

    if (response.ok) {
        alert('Cập nhật thành công!');
        fetchMovies();
        closeModal();
    } else {
        alert('Lỗi khi cập nhật phim');
    }
}
