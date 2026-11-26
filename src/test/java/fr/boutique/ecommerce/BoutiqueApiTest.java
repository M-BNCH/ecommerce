package fr.boutique.ecommerce;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import tools.jackson.databind.ObjectMapper;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BoutiqueApiTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;

    @Test
    void categoryCanContainBothASubcategoryAndAProduct() throws Exception {
        String sport = create("/api/categories", Map.of("name", "Sport", "description", ""));
        String ski = create("/api/categories", Map.of("name", "Ski", "description", ""));
        String jacket = create("/api/products", Map.of("name", "Veste", "price", 30, "stockQuantity", 3));

        send(put("/api/categories/{id}/parent", ski), Map.of("parentId", sport))
                .andExpect(status().isNoContent());
        send(put("/api/products/{id}/category", jacket), Map.of("categoryId", sport))
                .andExpect(status().isNoContent());

        mvc.perform(get("/api/categories/{id}", sport))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subcategories[0].id").value(ski))
                .andExpect(jsonPath("$.products[0].id").value(jacket));
    }

    @Test
    void cartTotalIncludesTheQuantityOfEachProduct() throws Exception {
        String cart = create("/api/carts", null);
        String gloves = create("/api/products", Map.of("name", "Gants", "price", 12.50, "stockQuantity", 4));
        String hat = create("/api/products", Map.of("name", "Bonnet", "price", 7, "stockQuantity", 1));

        var items = List.of(
                Map.of("productId", gloves, "quantity", 2),
                Map.of("productId", hat, "quantity", 1)
        );

        send(patch("/api/carts/{id}/items", cart), Map.of("items", items))
                .andExpect(status().isOk());

        mvc.perform(get("/api/carts/{id}", cart))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(2))
                .andExpect(jsonPath("$.total").value(32.0));
    }

    private String create(String path, Object body) throws Exception {
        var response = send(post(path), body)
                .andExpect(status().isCreated())
                .andReturn().getResponse();
        return json.readTree(response.getContentAsByteArray()).required("id").asString();
    }

    private ResultActions send(MockHttpServletRequestBuilder request, Object body) throws Exception {
        if (body != null) {
            request.contentType(APPLICATION_JSON).content(json.writeValueAsBytes(body));
        }
        return mvc.perform(request);
    }
}