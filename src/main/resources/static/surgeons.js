$(document).ready(function() {
    loadSurgeons();

    function loadSurgeons() {
        $.ajax({
            url: '/api/surgeons',
            method: 'GET',
            success: function(data) {
                const tableBody = $('#surgeonsTable tbody');
                tableBody.empty(); // Очистить таблицу перед добавлением данных
                data.forEach(surgeon => {
                    tableBody.append(`
                        <tr data-id="${surgeon.id}">
                            <td>${surgeon.firstName}</td>
                            <td>${surgeon.lastName}</td>
                            <td>${surgeon.middleName}</td>
                            <td>${new Date(surgeon.dateOfBirth).toLocaleDateString('ru-RU')}</td>
                            <td>${surgeon.yearsOfExperience}</td>
                            <td>${surgeon.education}</td>
                            <td>${surgeon.contactDetails}</td>
                            <td>${surgeon.specialization}</td>
                            <td>
                                <button class="action-btn edit-btn">✏️</button>
                                <button class="action-btn delete-btn">🗑️</button>
                            </td>
                        </tr>
                    `);
                });
            },
            error: function() {
                alert('Ошибка при загрузке данных хирургов.');
            }
        });
    }

    // Обработчик для редактирования хирурга
    $(document).on('click', '.edit-btn', function() {
        const surgeonId = $(this).closest('tr').data('id');
        $.ajax({
            url: `/api/surgeons/${surgeonId}`,
            method: 'GET',
            success: function(surgeon) {
                // Заполнение формы данными хирурга
                $('#editSurgeonId').val(surgeon.id);
                $('#editSurgeonFirstName').val(surgeon.firstName);
                $('#editSurgeonLastName').val(surgeon.lastName);
                $('#editSurgeonMiddleName').val(surgeon.middleName);
                $('#editSurgeonDateOfBirth').val(surgeon.dateOfBirth);
                $('#editSurgeonYearsOfExperience').val(surgeon.yearsOfExperience);
                $('#editSurgeonEducation').val(surgeon.education);
                $('#editSurgeonContactDetails').val(surgeon.contactDetails);
                $('#editSurgeonSpecialization').val(surgeon.specialization);

                // Показать модальное окно
                $('#editSurgeonModal').show();
            },
            error: function() {
                alert('Ошибка при загрузке данных хирурга.');
            }
        });
    });

    // Обработчик для сохранения изменений
    $('#saveEditSurgeon').click(function() {
        const surgeonId = $('#editSurgeonId').val();
        const updatedSurgeon = {
            firstName: $('#editSurgeonFirstName').val(),
            lastName: $('#editSurgeonLastName').val(),
            middleName: $('#editSurgeonMiddleName').val(),
            dateOfBirth: $('#editSurgeonDateOfBirth').val(),
            yearsOfExperience: $('#editSurgeonYearsOfExperience').val(),
            education: $('#editSurgeonEducation').val(),
            contactDetails: $('#editSurgeonContactDetails').val(),
            specialization: $('#editSurgeonSpecialization').val()
        };

        $.ajax({
            url: `/api/surgeons/${surgeonId}`,
            method: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(updatedSurgeon),
            success: function() {
                loadSurgeons(); // Перезагрузить список хирургов
                $('#editSurgeonModal').hide(); // Закрыть модальное окно
            },
            error: function() {
                alert('Ошибка при обновлении хирурга.');
            }
        });
    });

    // Обработчик для закрытия модального окна редактирования
    $('#closeEditModal').click(function() {
        $('#editSurgeonModal').hide();
    });

    // Обработчик для удаления хирурга
    $(document).on('click', '.delete-btn', function() {
        const surgeonId = $(this).closest('tr').data('id');
        if (confirm('Вы уверены, что хотите удалить этого хирурга?')) {
            $.ajax({
                url: `/api/surgeons/${ surgeonId}`,
                method: 'DELETE',
                success: function() {
                    loadSurgeons(); // Перезагрузить список хирургов
                },
                error: function() {
                    alert('Ошибка при удалении хирурга.');
                }
            });
        }
    });

    // Обработчик для добавления нового хирурга
    $('#saveNewSurgeon').click(function() {
        const newSurgeon = {
            firstName: $('#newSurgeonFirstName').val(),
            lastName: $('#newSurgeonLastName').val(),
            middleName: $('#newSurgeonMiddleName').val(),
            dateOfBirth: $('#newSurgeonDateOfBirth').val(),
            yearsOfExperience: $('#newSurgeonYearsOfExperience').val(),
            education: $('#newSurgeonEducation').val(),
            contactDetails: $('#newSurgeonContactDetails').val(),
            specialization: $('#newSurgeonSpecialization').val()
        };

        $.ajax({
            url: '/api/surgeons',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(newSurgeon),
            success: function() {
                loadSurgeons(); // Перезагрузить список хирургов
                $('#addSurgeonModal').hide(); // Закрыть модальное окно
            },
            error: function() {
                alert('Ошибка при добавлении хирурга.');
            }
        });
    });

    // Обработчик для закрытия модального окна добавления хирурга
    $('#closeNewModal').click(function() {
        $('#addSurgeonModal').hide();
    });
});