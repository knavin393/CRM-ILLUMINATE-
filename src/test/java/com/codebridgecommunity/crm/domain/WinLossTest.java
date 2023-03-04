package com.codebridgecommunity.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.codebridgecommunity.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WinLossTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WinLoss.class);
        WinLoss winLoss1 = new WinLoss();
        winLoss1.setId(1L);
        WinLoss winLoss2 = new WinLoss();
        winLoss2.setId(winLoss1.getId());
        assertThat(winLoss1).isEqualTo(winLoss2);
        winLoss2.setId(2L);
        assertThat(winLoss1).isNotEqualTo(winLoss2);
        winLoss1.setId(null);
        assertThat(winLoss1).isNotEqualTo(winLoss2);
    }
}
