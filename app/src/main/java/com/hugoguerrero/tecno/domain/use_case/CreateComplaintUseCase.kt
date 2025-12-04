package com.hugoguerrero.tecno.domain.use_case

import com.hugoguerrero.tecno.data.model.Complaint
import com.hugoguerrero.tecno.data.repository.ComplaintRepository
import javax.inject.Inject

class CreateComplaintUseCase @Inject constructor(
    private val complaintRepository: ComplaintRepository
) {
    suspend operator fun invoke(complaint: Complaint): Result<Unit> {
        return complaintRepository.createComplaint(complaint)
    }
}
