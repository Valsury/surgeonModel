$(document).ready(function() {
    loadPatients();

    function loadPatients() {
        $.ajax({
            url: '/api/patients',
            method: 'GET',
            success: function(data) {
                const tableBody = $('#patientsTable tbody');
                tableBody.empty();
                data.forEach(patient => {
                    tableBody.append(`
                        <tr data-id="${patient.id}">
                            <td>${patient.firstName}</td>
                            <td>${patient.lastName}</td>
                            <td>${patient.middleName}</td>
                            <td>${new Date(patient.dateOfBirth).toLocaleDateString('ru-RU')}</td>
                            <td>${patient.contactDetails}</td>
                            <td>${patient.passportSeries}</td>
                            <td>${patient.passportNumber}</td>
                            <td>
                                <button class="action-btn edit-btn" data-id="${patient.id}">✏️</button>
                                <button class="action-btn delete-btn" data-id="${patient.id}">🗑️</button>
                            </td>
                        </tr>
                    `);
                });
            },
            error: function() {
                alert('Ошибка при загрузке данных пациентов.');
            }
        });
    }

    $(document).on('click', '.edit-btn', function() {
        const patientId = $(this).data('id');
        $.ajax({
            url: `/api/patients/${patientId}`,
            method: 'GET',
            success: function(patient) {
                $('#editPatientId').val(patient.id);
                $('#editFirstName').val(patient.firstName);
                $('#editLastName').val(patient.lastName);
                $('#editMiddleName').val(patient.middleName);
                $('#editDateOfBirth').val(patient.dateOfBirth);
                $('#editContactDetails').val(patient.contactDetails);
                $('#editPassportSeries').val(patient.passportSeries);
                $('#editPassportNumber').val(patient.passportNumber);
                $('#editPatientModal').show();
            },
            error: function() {
                alert('Ошибка при загрузке данных пациента.');
            }
        });
    });

    $('#saveChanges').click(function() {
        const patientId = $('#editPatientId').val();
        const updatedPatient = {
            firstName: $('#editFirstName').val(),
            lastName: $('#editLastName').val(),
            middleName: $('#editMiddleName').val(),
            dateOfBirth: $('#editDateOfBirth').val(),
            contactDetails: $('#editContactDetails').val(),
            passportSeries: $('#editPassportSeries').val(),
            passportNumber: $('#editPassportNumber').val()
        };

        $.ajax({
            url: `/api/patients/${patientId}`,
            method: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(updatedPatient),
            success: function() {
                loadPatients();
                $('#editPatientModal').hide();
            },
            error: function() {
                alert('Ошибка при обновлении пациента.');
            }
        });
    });

    $('#closeEditModal').click(function() {
        $('#editPatientModal').hide();
    });

    $(document).on('click', '.delete-btn', function() {
        const patientId = $(this).data('id');
        if (confirm('Вы уверены, что хотите удалить этого пациента?')) {
            $.ajax({
                url: `/api/patients/${patientId}`,
                method: 'DELETE',
                success: function() {
                    loadPatients();
                },
                error: function() {
                    alert('Ошибка при удалении пациента.');
                }
            });
        }
    });

    $('#saveNewPatient').click(function() {
        const newPatient = {
            firstName: $('#newFirstName').val(),
            lastName: $('#newLastName').val(),
            middleName: $('#newMiddleName').val(),
            dateOfBirth: $('#newDateOfBirth').val(),
            contactDetails: $('#newContactDetails').val(),
            passportSeries: $('#newPassportSeries').val(),
            passportNumber: $('#newPassportNumber').val()
        };

        $.ajax({
            url: '/api/patients',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(newPatient),
            success: function() {
                loadPatients();
                $('#addPatientModal').hide();
            },
            error: function() {
                alert('Ошибка при добавлении пациента.');
            }
        });
    });

    $('#closeNewModal').click(function() {
        $('#addPatientModal').hide();
    });
});