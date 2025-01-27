package com.example.surgeonmodel.service;

import com.example.surgeonmodel.model.Appointment;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    public byte[] generatePdf(Appointment appointment) throws DocumentException, IOException {

        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);

        document.open();

        BaseFont baseFont;
        try (InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/arial.ttf")) {
            if (fontStream == null) {
                throw new RuntimeException("Шрифт arial.ttf не найден в ресурсах.");
            }
            baseFont = BaseFont.createFont("fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        }

        Font titleFont = new Font(baseFont, 18, Font.BOLD, BaseColor.DARK_GRAY);
        Font headerFont = new Font(baseFont, 12, Font.BOLD, BaseColor.WHITE);
        Font cellFont = new Font(baseFont, 12, Font.NORMAL, BaseColor.BLACK);

        Paragraph title = new Paragraph("Детали приема", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20f); // Отступ снизу
        document.add(title);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100); // Ширина таблицы 100% от страницы
        table.setSpacingBefore(10f); // Отступ сверху
        table.setSpacingAfter(10f); // Отступ снизу

        PdfPCell headerCell = new PdfPCell(new Phrase("Поле", headerFont));
        headerCell.setBackgroundColor(new BaseColor(0, 123, 255)); // Синий цвет
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setPadding(10);
        table.addCell(headerCell);

        headerCell = new PdfPCell(new Phrase("Значение", headerFont));
        headerCell.setBackgroundColor(new BaseColor(0, 123, 255)); // Синий цвет
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setPadding(10);
        table.addCell(headerCell);

        addTableRow(table, "Код приема", String.valueOf(appointment.getId()), cellFont);
        addTableRow(table, "Дата и время", formatDateTime(appointment.getAppointmentDateTime()), cellFont);
        addTableRow(table, "Тип приема", appointment.getAppointmentType(), cellFont);
        addTableRow(table, "Тип операции", appointment.getOperationType(), cellFont);
        addTableRow(table, "Хирург", formatName(appointment.getSurgeon()), cellFont);
        addTableRow(table, "Пациент", formatName(appointment.getPatient()), cellFont);

        document.add(table);

        document.add(new Paragraph(" ")); // Пустой параграф для отступа

        Paragraph signatureParagraph = new Paragraph("Подпись врача: ___________________", cellFont);
        signatureParagraph.setAlignment(Element.ALIGN_RIGHT); // Выравниваем по правому краю
        signatureParagraph.setSpacingBefore(20f); // Отступ сверху
        document.add(signatureParagraph);
        document.close();

        return outputStream.toByteArray();
    }

    private void addTableRow(PdfPTable table, String field, String value, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(field, font));
        cell.setPadding(10);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setBorder(Rectangle.BOX);
        table.addCell(cell);

        String displayValue = (value != null) ? value : "Не указано";
        cell = new PdfPCell(new Phrase(displayValue, font));
        cell.setPadding(10);
        cell.setBorder(Rectangle.BOX);
        table.addCell(cell);
    }

    private String formatDateTime(java.time.LocalDateTime dateTime) {
        if (dateTime == null) {
            return "Не указано";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return dateTime.format(formatter);
    }


    private String formatName(Object person) {
        if (person == null) {
            return "Не указано";
        }
        try {
            String firstName = (String) person.getClass().getMethod("getFirstName").invoke(person);
            String lastName = (String) person.getClass().getMethod("getLastName").invoke(person);
            return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
        } catch (Exception e) {
            return "Не указано";
        }
    }
}