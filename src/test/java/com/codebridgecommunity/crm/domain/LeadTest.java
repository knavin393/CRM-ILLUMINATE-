package com.codebridgecommunity.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.codebridgecommunity.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LeadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Lead.class);
        Lead lead1 = new Lead();
        lead1.setId(1L);
        Lead lead2 = new Lead();
        lead2.setId(lead1.getId());
        assertThat(lead1).isEqualTo(lead2);
        lead2.setId(2L);
        assertThat(lead1).isNotEqualTo(lead2);
        lead1.setId(null);
        assertThat(lead1).isNotEqualTo(lead2);
    }
}
