package com.riwi.microservice.coopcredit.credit.application.services;

import com.riwi.microservice.coopcredit.credit.domain.models.Affiliate;
import com.riwi.microservice.coopcredit.credit.domain.models.enums.AffiliateStatus;
import com.riwi.microservice.coopcredit.credit.domain.port.in.CreateAffiliateUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.in.DeleteAffiliateUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.in.ManageAffiliateStatusUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.in.RetrieveAffiliateUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.in.UpdateAffiliateUseCase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class AffiliateService implements CreateAffiliateUseCase, UpdateAffiliateUseCase, 
        RetrieveAffiliateUseCase, ManageAffiliateStatusUseCase, DeleteAffiliateUseCase {

    private final CreateAffiliateUseCase createAffiliateUseCase;
    private final UpdateAffiliateUseCase updateAffiliateUseCase;
    private final RetrieveAffiliateUseCase retrieveAffiliateUseCase;
    private final ManageAffiliateStatusUseCase manageAffiliateStatusUseCase;
    private final DeleteAffiliateUseCase deleteAffiliateUseCase;

    public AffiliateService(
            @Qualifier("createAffiliateUseCaseImpl") CreateAffiliateUseCase createAffiliateUseCase,
            @Qualifier("updateAffiliateUseCaseImpl") UpdateAffiliateUseCase updateAffiliateUseCase,
            @Qualifier("retrieveAffiliateUseCaseImpl") RetrieveAffiliateUseCase retrieveAffiliateUseCase,
            @Qualifier("manageAffiliateStatusUseCaseImpl") ManageAffiliateStatusUseCase manageAffiliateStatusUseCase,
            @Qualifier("deleteAffiliateUseCaseImpl") DeleteAffiliateUseCase deleteAffiliateUseCase) {
        this.createAffiliateUseCase = createAffiliateUseCase;
        this.updateAffiliateUseCase = updateAffiliateUseCase;
        this.retrieveAffiliateUseCase = retrieveAffiliateUseCase;
        this.manageAffiliateStatusUseCase = manageAffiliateStatusUseCase;
        this.deleteAffiliateUseCase = deleteAffiliateUseCase;
    }

    @Override
    public Affiliate createAffiliate(CreateAffiliateCommand command) {
        return createAffiliateUseCase.createAffiliate(command);
    }

    @Override
    public Affiliate updateAffiliate(Long affiliateId, UpdateAffiliateCommand command) {
        return updateAffiliateUseCase.updateAffiliate(affiliateId, command);
    }

    @Override
    public Optional<Affiliate> getAffiliateById(Long affiliateId) {
        return retrieveAffiliateUseCase.getAffiliateById(affiliateId);
    }

    @Override
    public Optional<Affiliate> getAffiliateByDocument(String document) {
        return retrieveAffiliateUseCase.getAffiliateByDocument(document);
    }

    @Override
    public List<Affiliate> getAllAffiliates(int page, int size) {
        return retrieveAffiliateUseCase.getAllAffiliates(page, size);
    }

    @Override
    public List<Affiliate> getAffiliatesByStatus(AffiliateStatus status, int page, int size) {
        return retrieveAffiliateUseCase.getAffiliatesByStatus(status, page, size);
    }

    @Override
    public Affiliate changeAffiliateStatus(Long affiliateId, AffiliateStatus newStatus) {
        return manageAffiliateStatusUseCase.changeAffiliateStatus(affiliateId, newStatus);
    }

    @Override
    public void deleteAffiliate(Long affiliateId) {
        deleteAffiliateUseCase.deleteAffiliate(affiliateId);
    }
}
