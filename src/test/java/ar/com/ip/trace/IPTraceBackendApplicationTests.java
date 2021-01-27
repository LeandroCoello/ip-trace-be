package ar.com.ip.trace;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.google.gson.Gson;

import ar.com.ip.trace.dto.ErrorResponseDTO;
import ar.com.ip.trace.dto.IPRequestDTO;
import ar.com.ip.trace.dto.IPTraceResponseDTO;
import ar.com.ip.trace.dto.StatsResponseDTO;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IPTraceBackendApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	
	private static Gson gson;

    @BeforeClass
    public static void init() {
       gson = new Gson();
    }	
	
	@Test
	public void contextLoads() {
	}

	/**
	 * Ping test
	 * 
	 */
	@Test
	public void getPing() throws Exception {
	    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/ping");

	    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
	    int status = result.getResponse().getStatus();
	    String stringResponse = result.getResponse().getContentAsString();
	    
	    assertEquals(200, status);
	    assertEquals("OK",stringResponse);
	}
	
	/**
	 * Test de un caso de Argentina con 2 repeticiones:
	 * La primera consume servicios y persiste en la base.
	 * La segunda obtiene de la base y completa calculando los datos necesarios.
	 */
	@Test
	@Repeat(2)
	public void getTraceArg() throws Exception {
	    
	    MvcResult result = callApi("181.23.238.193");
	    IPTraceResponseDTO response = gson.fromJson(result.getResponse().getContentAsString(), IPTraceResponseDTO.class);
	    int status = result.getResponse().getStatus();
	    
		assertEquals(200 ,status);		
		assertEquals("181.23.238.193",response.getIp());
		assertEquals("Argentina",response.getCountry());
		assertEquals("AR",response.getIso_code());
		assertEquals(2,response.getLanguages().size());
		assertEquals("Spanish (es)", response.getLanguages().get(0));
		assertEquals("520 kms", response.getEstimated_distance());
	}

	/**
	 * Test de un caso de Alemania.
	 */
	@Test
	public void getTraceGer() throws Exception {
		
	    MvcResult result = callApi("5.6.7.8");
	    IPTraceResponseDTO response = gson.fromJson(result.getResponse().getContentAsString(), IPTraceResponseDTO.class);
	    int status = result.getResponse().getStatus();
		
		assertEquals(200 ,status);		
		assertEquals("5.6.7.8",response.getIp());
		assertEquals("Germany",response.getCountry());
		assertEquals("DE",response.getIso_code());
		assertEquals(1,response.getLanguages().size());
		assertEquals("German (de)", response.getLanguages().get(0));
		assertEquals("11565 kms", response.getEstimated_distance());	
	}
	
	/**
	 * Test de un caso de status code 400.
	 */
	@Test
	public void getTraceError() throws Exception {
		
	    MvcResult result = callApi("");
	    ErrorResponseDTO response = gson.fromJson(result.getResponse().getContentAsString(), ErrorResponseDTO.class);
	    int status = result.getResponse().getStatus();
	    
		assertEquals(400 ,status);
		assertEquals(400 ,response.getStatus());
		assertEquals("Null body or IP.", response.getMsg());
		
	}
	
	/**
	 * Test de stats:
	 * Cantidad de invocaciones de Argentina: 2
	 * Cantidad de invocaciones de Alemania: 1
	 * Promedio: (11565 + 520*2) / 3 = 4202
	 */
	@Test
	public void getStats() throws Exception {
		
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/stats");

	    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
	    StatsResponseDTO response = gson.fromJson(result.getResponse().getContentAsString(), StatsResponseDTO.class);
	    int status = result.getResponse().getStatus();
		
		assertEquals(200,status);
		assertEquals(new Long(520) ,response.getMinDistance());
		assertEquals(new Long(11565), response.getMaxDistance());
		assertEquals(new Long(4202), response.getAvgDistance());
		
	}

	private MvcResult callApi(String ip) throws Exception{
		IPRequestDTO request = new IPRequestDTO();
		request.setIp(ip);
		MockHttpServletRequestBuilder requestBuilder = 
				MockMvcRequestBuilders.post("/trace")
				.contentType(MediaType.APPLICATION_JSON)
				.content(gson.toJson(request));
		
	    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		return result;
	}
	
}
