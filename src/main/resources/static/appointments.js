$(document).ready(function() {
    loadAppointments();

    function loadAppointments() {
        $.ajax({
            url: '/api/appointments',
            method: 'GET',
            success: function(data) {
                console.log("Данные о приемах:", data); // Логируем данные
                const tableBody = $('#appointmentsTable tbody');
                tableBody.empty();
                data.forEach(appointment => {
                    tableBody.append(`
                <tr>
                    <td>${appointment.id}</td>
                    <td>${new Date(appointment.appointmentDateTime).toLocaleString('ru-RU')}</td>
                    <td>${appointment.appointmentType}</td>
                    <td>${appointment.operationType}</td>
                    <td>${appointment.surgeonName}</td>
                    <td>${appointment.patientName}</td>
                    <td>
                        <button class="edit-btn" data-id="${appointment.id}">✏️</button>
                        <button class="delete-btn" data-id="${appointment.id}">🗑️</button>
                        <button class="print-btn" data-id="${appointment.id}">🖨️</button>
                    </td>
                </tr>
            `);
                });
            },
            error: function() {
                alert('Ошибка при загрузке данных приемов.');
            }
        });
    }

    // Обработчик для добавления нового приема
    $('#addAppointmentBtn').click(function() {
        $('#modalTitle').text('Добавить прием');
        $('#appointmentId').val('');
        $('#appointmentDateTime').val('');
        $('#appointmentType').val('');
        $('#operationType').val('');
        $('#surgeonId').val('');
        $('#patientId').val('');
        $('#appointmentModal').show(); // Показать модальное окно
    });

    // Обработчик для сохранения приема
    $('#saveAppointment').click(function() {
        const appointmentId = $('#appointmentId').val();
        const newAppointment = {
            appointmentDateTime: $('#appointmentDateTime').val(),
            appointmentType: $('#appointmentType').val(),
            operationType: $('#operationType').val(),
            surgeon: { id: $('#surgeonId').val() },
            patient: { id: $('#patientId').val() }
        };

        const method = appointmentId ? 'PUT' : 'POST';
        const url = appointmentId ? `/api/appointments/${appointmentId}` : '/api/appointments';

        $.ajax({
            url: url,
            method: method,
            contentType: 'application/json',
            data: JSON.stringify(newAppointment),
            success: function() {
                loadAppointments(); // Перезагрузить список приемов
                $('#appointmentModal').hide(); // Закрыть модальное окно
            },
            error: function() {
                alert('Ошибка при сохранении приема.');
            }
        });
    });

    // Обработчик для редактирования приема
    $(document).on('click', '.edit-btn', function() {
        const appointmentId = $(this).data('id');
        $.ajax({
            url: `/api/appointments/${appointmentId}`,
            method: 'GET',
            success: function(appointment) {
                $('#modalTitle').text('Редактировать прием');
                $('#appointmentId').val(appointment.id);
                $('#appointmentDateTime').val(new Date(appointment.appointmentDateTime).toISOString().slice(0, 16));
                $('#appointmentType').val(appointment.appointmentType);
                $('#operationType').val(appointment.operationType);
                $('#surgeonId').val(appointment.surgeon.id);
                $('#patientId').val(appointment.patient.id);
                $('#appointmentModal').show(); // Показать модальное окно
            },
            error: function() {
                alert('Ошибка при загрузке данных приема.');
            }
        });
    });

    // Обработчик для удаления приема
    $(document).on('click', '.delete-btn', function() {
        const appointmentId = $(this).data('id');
        if (confirm('Вы уверены, что хотите удалить этот прием?')) {
            $.ajax({
                url: `/api/appointments/${appointmentId}`,
                method: 'DELETE',
                success: function() {
                    loadAppointments(); // Перезагрузить список приемов
                },
                error: function() {
                    alert('Ошибка при удалении приема.');
                }
            });
        }
    });

    // Обработчик для печати приема (генерация PDF)
    $(document).on('click', '.print-btn', function() {
        const appointmentId = $(this).data('id');

        // Отправляем запрос на сервер для генерации PDF
        window.open(`/api/pdf/appointments/${appointmentId}`, '_blank');
    });

    // Обработчик для закрытия модального окна
    $('#closeModal').click(function() {
        $('#appointmentModal').hide(); // Скрыть модальное окно
    });

    // Закрытие модального окна при клике вне его
    $(window).click(function(event) {
        if (event.target.id === 'appointmentModal') {
            $('#appointmentModal').hide(); // Скрыть модальное окно
        }
    });
});