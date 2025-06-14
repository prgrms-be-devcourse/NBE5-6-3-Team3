package com.grepp.mailservice.controller

import com.grepp.mailservice.dto.CodeMailRequest
import com.grepp.mailservice.dto.CodeMailResponse
import com.grepp.mailservice.dto.HtmlMailRequest
import com.grepp.mailservice.service.MailSendService
import com.grepp.response.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/mail")
class MailController(private val mailSendService: MailSendService) {

    @PostMapping("/send-code")
    fun sendSimpleMail(@RequestBody request: CodeMailRequest): ApiResponse<CodeMailResponse> {
        val code = mailSendService.sendCodeMail(request.to, request.codeType)
        return ApiResponse.noContent()
    }

    @PostMapping("/send-html")
    fun sendSettlementMail(@RequestBody request: HtmlMailRequest): ApiResponse<Void> {
        mailSendService.sendHtmlMail(request)
        return ApiResponse.noContent()
    }

}