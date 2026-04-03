package com.giwon.shelternow

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
class ShelterNowControllerTest(
    @Autowired private val mockMvc: MockMvc,
) {
    @Test
    fun `shelter list returns success`() {
        mockMvc.get("/api/v1/shelters")
            .andExpect {
                status { isOk() }
                jsonPath("$.success") { value(true) }
            }
    }
}
