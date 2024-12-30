$(document).ready(function() {
    $.ajax({
        url: '/api/surgeons',
        method: 'GET',
        success: function(data) {
            const tableBody = $('#surgeonsTable tbody');
            data.forEach(surgeon => {
                tableBody.append(`
                    <tr>
                        <td>${surgeon.firstName}</td>
                        <td>${surgeon.lastName}</td>
                        <td>${surgeon.middleName}</td>
                        <td>${new Date(surgeon.dateOfBirth).toLocaleDateString('ru-RU')}</td>
                        <td>${surgeon.yearsOfExperience}</td>
                        <td>${surgeon.education}</td>
                        <td>${surgeon.contactDetails}</td>
                        <td>${surgeon.username}</td>
                    </tr>
                `);
            });
        },
        error: function() {
            alert('Ошибка при загрузке данных хирургов.');
        }
    });
});

