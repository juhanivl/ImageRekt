package imageRektServlet;

import imageRektDB.Image;
import imageRektDB.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author patricka
 */
@WebServlet(name = "Upload", urlPatterns = {"/upload"})
// save images here, so that they can be easily accessed from outside
@MultipartConfig(location = "/var/www/html/test/")
public class FileUploadServlet extends HttpServlet {

    EntityManagerFactory emf;
    EntityManager em;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            request.getPart("file").write(request.getPart("file").getSubmittedFileName());
            out.println("File uploaded successfully! " + request.getPart("file").getSubmittedFileName());
        } catch (Exception e) {
            out.println("Exception -->" + e.getMessage());
        } finally {
            //out.close();
        }
                
        /* works for creating a new user
        emf = Persistence.createEntityManagerFactory("ImageRektPU");
        em = emf.createEntityManager();
        em.getTransaction().begin();
        User newUser = new User(779);
        newUser.setUname("again");
        newUser.setUemail("21123");
        newUser.setUpass("servlet");
        em.persist(newUser);
        em.getTransaction().commit(); */
        
        //create a new transaction to add data about the image upload to DB.
        emf = Persistence.createEntityManagerFactory("ImageRektPU");
        em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            out.println("here we go<br>");
            User u = (User)em.createNamedQuery("User.findByUid").setParameter("uid", 1).getSingleResult();
            out.println(u.getUname() + "<br>");
            //image title, description, Date generated in java the name of the file
            Image img = new Image("UBER title", "desc", new Date(), request.getPart("file").getSubmittedFileName(), u);
            out.println(img.getTitle() + "<br>");
            em.persist(img);
            em.getTransaction().commit(); 
            out.println("File in DB successfully!");
        }catch(Exception e){
            out.println("BOOM! " + e);
        }
        emf.close();
    }

   
}
