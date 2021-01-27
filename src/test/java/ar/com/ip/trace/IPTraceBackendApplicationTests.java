package ar.com.ip.trace;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.junit4.SpringRunner;

import ar.com.ip.trace.dto.IPRequestDTO;
import ar.com.ip.trace.dto.IPTraceResponseDTO;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {IPTraceBackendApplication.class})
public class IPTraceBackendApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;	

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Test
	public void contextLoads() {
	}

	@Test
	public void getPing() throws Exception {
		ResponseEntity<String> responseEntity = restTemplate.getForEntity("/ping", String.class);
		assertEquals(HttpStatus.OK ,responseEntity.getStatusCode());
		assertEquals("OK",responseEntity.getBody());

	}
	
	@Test
	@Repeat(2)
	public void getTraceArg() throws Exception {

		ResponseEntity<IPTraceResponseDTO> responseEntity = callApi("181.23.238.193");
		IPTraceResponseDTO body = responseEntity.getBody();
		
		assertEquals(HttpStatus.OK ,responseEntity.getStatusCode());		
		assertEquals("181.23.238.193",body.getIp());
		assertEquals("Argentina",body.getCountry());
		assertEquals("AR",body.getIso_code());
		assertEquals(2,body.getLanguages().size());
		assertEquals("Spanish (es)", body.getLanguages().get(0));
		assertEquals("520 kms", body.getEstimated_distance());

	}

	@Test
	public void getTraceGer() throws Exception {
		
		ResponseEntity<IPTraceResponseDTO> responseEntity = callApi("5.6.7.8");
		IPTraceResponseDTO body = responseEntity.getBody();
		
		assertEquals(HttpStatus.OK ,responseEntity.getStatusCode());		
		assertEquals("5.6.7.8",body.getIp());
		assertEquals("Germany",body.getCountry());
		assertEquals("DE",body.getIso_code());
		assertEquals(1,body.getLanguages().size());
		assertEquals("German (de)", body.getLanguages().get(0));
		assertEquals("11565 kms", body.getEstimated_distance());
		
	}

	@Test
	public void getTraceError() throws Exception {
		
		ResponseEntity<IPTraceResponseDTO> responseEntity = callApi("");
		IPTraceResponseDTO body = responseEntity.getBody();
		
		assertEquals(HttpStatus.BAD_REQUEST ,responseEntity.getStatusCode());
		
	}

	private ResponseEntity<IPTraceResponseDTO> callApi(String ip){
		ParameterizedTypeReference<IPTraceResponseDTO> listType = new ParameterizedTypeReference<IPTraceResponseDTO>(){};
		IPRequestDTO ipRequestDTO = new IPRequestDTO();
		ipRequestDTO.setIp(ip);
		
		HttpEntity<IPRequestDTO> requestEntity = new HttpEntity<IPRequestDTO>(ipRequestDTO); 
		ResponseEntity<IPTraceResponseDTO> responseEntity = restTemplate.exchange("/trace", HttpMethod.POST, requestEntity, listType);

		return responseEntity;
	}
	
}
