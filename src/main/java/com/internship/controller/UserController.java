package com.internship.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.internship.dao.ContactRepository;
import com.internship.dao.UserRepository;
import com.internship.entities.Contact;
import com.internship.entities.User;
import com.internship.helper.Message;

import net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContactRepository contactRepository;
	
	
	//method for adding common data to response
	@ModelAttribute
	public void addCommonData(Model model,Principal principal) {
		
		
		String userName=principal.getName();
		System.out.println("USERNAME "+userName);
		
		User user=userRepository.getUserByUserName(userName);
		
		System.out.println("USER "+user);
		
		model.addAttribute("user",user);
		
		
		
	}
	
	//dashboard home
	@RequestMapping("/index")
	public String dashboard(Model model,Principal principal) {
		
		model.addAttribute("title","User Dashboard");
		
		//get the user using userName
		
		return "normal/user_dashboard";
	}
	
	//open add form handler
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {
		
		model.addAttribute("title","Add Contact");
		model.addAttribute("contact",new Contact());
		return "normal/add_contact_form";
	}
	
	//processing and contact form
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact,
			@RequestParam("profileImage") MultipartFile file,
			Principal principal,HttpSession session) {
		
		try {
		String name=principal.getName();
		User user=this.userRepository.getUserByUserName(name);
		
		
		
		
		//processing and uploading file
		if(file.isEmpty()) {
			System.out.println("File is empty");
			contact.setImage("contact.png");
		}
		else {
			contact.setImage(file.getOriginalFilename());
			File saveFile=new ClassPathResource("static/img").getFile();
		Path path=	Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			
			Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Image uploaded");
			
		}
		
		user.getContacts().add(contact);
		contact.setUser(user);
		
		
		this.userRepository.save(user);
		
		System.out.println("DATA "+contact);
		System.out.println("Added to database ");
		//message success
		session.setAttribute("message", new Message("Your contact is added !! Add more..","success"));
		
		
		}
		catch(Exception e) {
			System.out.println("ERROR "+e.getMessage());
			e.printStackTrace();
			//error message
			session.setAttribute("message", new Message("Something went wrong!! Try again..","danger"));
		}
		return "normal/add_contact_form";
	}
	
	//show contacts handler
	//per page =5[n]
	//current page =0 [page]
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page,Model m,Principal principal) {
		m.addAttribute("title","Show User contacts");
		//sending contacts from database
		String userName=principal.getName();
		User user=this.userRepository.getUserByUserName(userName);
		org.springframework.data.domain.Pageable pageable= PageRequest.of(page, 5);
		Page<Contact> contacts=this.contactRepository.findContactsByUser(user.getId(),pageable);
		m.addAttribute("contacts",contacts);
		m.addAttribute("currentPage",page);
		m.addAttribute("totalPages",contacts.getTotalPages());
		return "normal/show_contacts";
	}
	
	//contact details
	@RequestMapping("/{cId}/contact")
	public String showContactDetail(@PathVariable("cId") Integer cId,Model model) {
		System.out.println("CID "+cId);
		
		java.util.Optional<Contact> contactOptional=this.contactRepository.findById(cId);
		Contact contact=contactOptional.get();
		
		model.addAttribute("contact",contact);
		
		return "normal/contact_detail";
	}
	
	//delete
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cId,Model model,HttpSession session,Principal principal) {
		
		//java.util.Optional<Contact> contactOptional= this.contactRepository.findById(cId);
		Contact contact= this.contactRepository.findById(cId).get();
			//	contactOptional.get();
		
		System.out.println("Contact "+contact.getcId());
		
		User user=this.userRepository.getUserByUserName(principal.getName());
		user.getContacts().remove(contact);
		this.userRepository.save(user);
		//contact.setUser(null);
		
		
		
		
		
		//check
		this.contactRepository.delete(contact);
		session.setAttribute("message", new Message("Contact deleted succesfully...","success"));
		
		return "redirect:/user/show-contacts/0";
	}
	
	//update form
	@PostMapping("/update-contact/{cid}")
	public String updateForm(@PathVariable("cid") Integer cid,Model m) {
		
		m.addAttribute("title","Update Contact");
		
		Contact contact=this.contactRepository.findById(cid).get();
		m.addAttribute("contact",contact);
		return "normal/update_form";
	}
	
	//update contact handler
	@RequestMapping(value="/process-update",method=RequestMethod.POST)
	public String updateHandler(@ModelAttribute Contact contact,
			@RequestParam("profileImage") MultipartFile file,
			Model m,HttpSession session,Principal principal) {
		
		try {
			//old contact detail
			Contact oldcontactDetail=this.contactRepository.findById(contact.getcId()).get();
			
			if(!file.isEmpty()) {
				//rewrite
				//delete old photo
				File deleteFile=new ClassPathResource("static/img").getFile();
				File file1=new File(deleteFile,oldcontactDetail.getImage());
				file1.delete();
				
				
				//update new photo
				
				File saveFile=new ClassPathResource("static/img").getFile();
				Path path=	Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
					
					Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
				
				contact.setImage(file.getOriginalFilename());
			}
			else {
				contact.setImage(oldcontactDetail.getImage());
			}
			User user=this.userRepository.getUserByUserName(principal.getName());
			contact.setUser(user);
			this.contactRepository.save(contact);
			session.setAttribute("message", new Message("Your Contact is updated...","Success"));
			
		}
		catch(Exception e) {
			
		}
		System.out.println("CONTACT NAME "+contact.getName());
		System.out.println("CONTACT ID "+contact.getcId());
		return "redirect:/user/"+contact.getcId()+"/contact";
	}
	
	//your profile handler
	@GetMapping("/profile")
	public String yourProfile(Model model) {
		
		model.addAttribute("title","Profile Page");
		
		
		return "normal/profile";
	}
	
	//open setting handler
	@GetMapping("/settings")
	public String openSettings() {
		
		return "normal/settings";
	}
	
	//change password handler
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword,Principal principal,HttpSession session) {
		
		
		
		System.out.println("OLD PASSWORD "+oldPassword);
		System.out.println("NEW PASSWORD "+newPassword);
		String userName = principal.getName();
		User currentUser = this.userRepository.getUserByUserName(userName);
		System.out.println(currentUser.getPassword());
		if(this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())) {
			
			//change password
			currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
			this.userRepository.save(currentUser);
			session.setAttribute("message",new Message("Your password is successfully changed","success"));
		}
		else {
			//error
			session.setAttribute("message", new Message("Wrong old password!!","danger"));
			return "redirect:/user/settings";
		}
		
		
		return "redirect:/user/index";
	}

	
	
	
	
}
