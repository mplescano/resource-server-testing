package com.github.timtebeek.rst.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashSet;

import com.github.timtebeek.rst.MyApp;
import com.github.timtebeek.rst.OAuthHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MyApp.class)
@WebAppConfiguration
public class MyControllerTest {
	@Autowired
	private WebApplicationContext webapp;

	private MockMvc mvc;

	@Before
	public void before() {
		mvc = MockMvcBuilders.webAppContextSetup(webapp).apply(springSecurity()).build();
	}

	@Autowired
	private OAuthHelper helper;

	@Test
	public void testHelloWithRole() throws Exception {
		RequestPostProcessor bearerToken = helper.bearerToken("myclientwith", new HashSet<>(Arrays.asList("myscope")), "user");
		mvc.perform(get("/hello").with(bearerToken)).andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void testHelloWithoutRole() throws Exception {
		RequestPostProcessor bearerToken = helper.bearerToken("myclientwithout", new HashSet<>(Arrays.asList("noscope")), "user");
		mvc.perform(get("/hello").with(bearerToken)).andDo(print()).andExpect(status().isForbidden());
	}
}
