package itis.ecozubrbot.repositories.impl;

import itis.ecozubrbot.model.MessageTimer;
import itis.ecozubrbot.repositories.MessageTimerRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MapMessageTimerRepository implements MessageTimerRepository {

    ConcurrentHashMap<Long, List<MessageTimer>> messageTimerMap = new ConcurrentHashMap<>();

    @Override
    public List<MessageTimer> findByNewsLetterId(long newsletterId) {
        return messageTimerMap.get(newsletterId);
    }

    @Override
    public void save(MessageTimer messageTimer) {
        long newsLetterId = messageTimer.getNewsletterId();
        List<MessageTimer> list = messageTimerMap.get(newsLetterId);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(messageTimer);
        messageTimerMap.put(newsLetterId, list);
    }

    @Override
    public void update(MessageTimer messageTimer) {
        long newsletterId = messageTimer.getNewsletterId();
        List<MessageTimer> list = messageTimerMap.get(newsletterId);

        String uuid = messageTimer.getUuid();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUuid().equals(uuid)) {
                list.set(i, messageTimer);
                break;
            }
        }
        messageTimerMap.put(newsletterId, list);
    }

    @Override
    public void delete(MessageTimer messageTimer) {
        long newsletterId = messageTimer.getNewsletterId();
        List<MessageTimer> list = messageTimerMap.get(newsletterId);

        String uuid = messageTimer.getUuid();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUuid().equals(uuid)) {
                list.remove(i);
                break;
            }
        }
        messageTimerMap.put(newsletterId, list);
    }
}
