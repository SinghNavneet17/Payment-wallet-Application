package com.paykaro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paykaro.exception.BeneficiaryException;
import com.paykaro.exception.CustomerException;
import com.paykaro.model.Beneficiary;
import com.paykaro.model.CurrentUserSession;
import com.paykaro.model.Customer;
import com.paykaro.repository.BeneficiaryDAO;
import com.paykaro.repository.CustomerDAO;
import com.paykaro.repository.SessionDAO;

@Service
public class BeneficiaryServiceImpl implements BeneficiaryService {

	@Autowired
	private SessionDAO sessionDAO;

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private BeneficiaryDAO beneficiaryDAO;

	@Override
	public Beneficiary addBeneficiary(Beneficiary beneficiary, String key)
			throws BeneficiaryException, CustomerException {

		CurrentUserSession loggedInUser = sessionDAO.findByUuid(key);

		if (loggedInUser == null) {
			throw new CustomerException("Please provide a valid key to  add beneficiary...");
		}

		customerDAO.findById(beneficiary.getCustomer().getCid())
				.orElseThrow(() -> new CustomerException("customer not found..create account"));
		return beneficiaryDAO.save(beneficiary);

	}

	@Override
	public Beneficiary deleteBeneficiary(Beneficiary beneficiary, String key) throws CustomerException {
		CurrentUserSession loggedInUser = sessionDAO.findByUuid(key);

		if (loggedInUser == null) {
			throw new CustomerException("Please provide a valid key to  add beneficiary...");
		}

		beneficiaryDAO.delete(beneficiary);

		return beneficiary;
	}

	@Override
	public List<Beneficiary> viewBeneficiaries(String mobileNo, String key)
			throws CustomerException, BeneficiaryException {

		CurrentUserSession loggedInUser = sessionDAO.findByUuid(key);

		if (loggedInUser == null) {
			throw new CustomerException("Please provide a valid key to  add beneficiary...");
		}

		List<Beneficiary> beneficiaries = beneficiaryDAO.findByMobileNo(mobileNo);
		if (beneficiaries.isEmpty())
			throw new BeneficiaryException("NO beneficiary found....");

		return beneficiaries;
	}

}
