package com.example.lonelyPlanet;

import com.example.lonelyPlanet.Model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

//importe tout ce qui va permettre de simuler des requetes : ex : quelqu'un qui va sur la page hello
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
class LonelyPlanetApplicationTests {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper mapper;

	private MockMvc mvc;

	//on met pas en static car sinon il n'arrive pas à faire l'autowired donc pas de beforeAll
	@BeforeEach
	public void beforeEach(){
		mvc= MockMvcBuilders
				//récupération du contexte de notre application : les dao...
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();
	}

	@Test
			//il faut commenter le withmockuser quand on fait un test sur l'inscrition et authentification
//	@WithMockUser(username = "admin", roles=("ADMIN"))
	void contextLoads() throws Exception {
		//attention à chaque nouveau test : changer le login de l'utilisateur sinon ca marche pas : utiliser math.random
		User user = new User();
		user.setLogin("testauthentification4");
		user.setPassword("testmdp");
		String json = mapper.writeValueAsString(user);


//		//vérifier si ca accepte et si ca contient un JSON
//		mvc.perform(put("/users")
//				.contentType(MediaType.APPLICATION_JSON)
//				.accept(MediaType.APPLICATION_JSON)
//				.content(json))
//				//on vérifie que ca contient du JSON
////				.contentType(MediaType.APPLICATION_JSON))
//				//on vérifie le bon statut de la requete :200
//				.andExpect(status().isOk());

		//on teste le contenu du json  : on veut vérifier qu'on récupère bien le bon utilisateur : avec le bon login
//		mvc.perform(get("/users"))
//				.andExpect(MockMvcResultMatchers
//				.jsonPath("$[0].login")
//				.value("ju")
//				);

		//on vérifie le contenu d'un json pour un utilisateur en particulier
//		mvc.perform(get("/users/3"))
//				.andExpect(MockMvcResultMatchers
//						.jsonPath("$.login")
//						.value("ju")
//				);

		//on teste l'inscription d'un utilisateur
		mvc.perform(
				put("/inscription")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isOk());

		//on teste l'authentification par token d'un utilisateur : on récupère son token
		String token = mvc.perform(
				post("/authentification")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();
		System.out.println(token);

		//on teste une requête que l'utilisateur a droit de faire
		mvc.perform(get("/categories")
				.header("authorization","Bearer"+ token)
		)
				.andExpect(status().isOk());

	}

}
