package itis.ecozubrbot.service.newsletterwithtimer;

public interface ModerationService {
    void setResultNewsletterTimer(long idNewsletter, long countApproved, long countRejected);
}
