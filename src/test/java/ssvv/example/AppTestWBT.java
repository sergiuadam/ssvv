package ssvv.example;
import static org.junit.Assert.assertEquals;
import domain.Nota;
import domain.Student;
import domain.Tema;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import repository.NotaXMLRepository;
import repository.StudentXMLRepository;
import repository.TemaXMLRepository;
import service.Service;
import validation.*;

public class AppTestWBT
{
    private Service service;

    private void initializeFile(String name) {
        try {
            File newFile = new File(name);
            if (newFile.createNewFile()) {
                System.out.println("File created: " + newFile.getName());
            } else {
                System.out.println("File already exists.");
            }
            FileWriter fileWriter = new FileWriter(name);
            fileWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                    "<Entitati>\n" +
                    "\n" +
                    "</Entitati>\n");
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    @Before
    public void init() {
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Tema> temaValidator = new TemaValidator();
        Validator<Nota> notaValidator = new NotaValidator();
        initializeFile("studenti_test.xml");
        initializeFile("teme_test.xml");
        initializeFile("note_test.xml");
        StudentXMLRepository studentRepo = new StudentXMLRepository(studentValidator, "studenti_test.xml");
        TemaXMLRepository temeRepo = new TemaXMLRepository(temaValidator, "teme_test.xml");
        NotaXMLRepository noteRepo = new NotaXMLRepository(notaValidator, "note_test.xml");
        service = new Service(studentRepo, temeRepo, noteRepo);
    }

    @After
    public void cleanup() {
        File fisier = new File("studenti_test.xml");
        fisier.delete();
        fisier = new File("teme_test.xml");
        fisier.delete();
        fisier = new File("note_test.xml");
        fisier.delete();
    }


    @Test
    public void test_empty_description_addAssignment(){
        assertEquals(0, service.findAllTeme().spliterator().getExactSizeIfKnown());

        try{
            service.saveTema("99", "", 8, 3);
        }
        catch (ValidationException e) {
            assertEquals("Descriere nula! \n", e.getMessage());
        }
        assertEquals(0, service.findAllTeme().spliterator().getExactSizeIfKnown());
    }

    @Test
    public void test_null_description_addAssignment() {
        assertEquals(0, service.findAllTeme().spliterator().getExactSizeIfKnown());
        try{
            service.saveTema("99", null, 8, 3);
        }
        catch (ValidationException e) {
            assertEquals("Descriere invalida! \n", e.getMessage());
        }
        assertEquals(0, service.findAllTeme().spliterator().getExactSizeIfKnown());
    }

}
