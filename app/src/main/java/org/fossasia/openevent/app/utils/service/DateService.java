package org.fossasia.openevent.app.utils.service;

import org.fossasia.openevent.app.data.event.Event;
import org.fossasia.openevent.app.utils.DateUtils;
import org.threeten.bp.ZonedDateTime;

import java.text.ParseException;

@SuppressWarnings("PMD.DataflowAnomalyAnalysis") // Bug in PMD related to DU anomaly
public final class DateService {

    public static final String LIVE_EVENT = "LIVE";
    public static final String PAST_EVENT = "PAST";
    public static final String UPCOMING_EVENT = "UPCOMING";

    private DateService() {
        // Never Called
    }

    /**
     * Compare events for sorting
     * the list will be in order of live events, upcoming events, past events
     *
     * for both live events latest will be before in list
     * for both past events lately ended will be before in list
     * for both upcoming started earliest will be before in list
     *
     * @return int
     */
    public static int compareEventDates(Event one, Event two) {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime startDate = DateUtils.getDate(one.getStartsAt().get());
        ZonedDateTime endDate = DateUtils.getDate(one.getEndsAt().get());
        ZonedDateTime otherStartDate = DateUtils.getDate(two.getStartsAt().get());
        ZonedDateTime otherEndDate = DateUtils.getDate(two.getEndsAt().get());
        if (endDate.isBefore(now) || otherEndDate.isBefore(now)) {
            // one of them is past and other can be past or live or upcoming
            return endDate.isAfter(otherEndDate) ? -1 : 1;
        } else {
            if (startDate.isAfter(now) || otherStartDate.isAfter(now)) {
                // one of them is upcoming other can be upcoming or live
                return startDate.isBefore(otherStartDate) ? -1 : 1;
            } else {
                // both are live
                return startDate.isAfter(otherStartDate) ? -1 : 1;
            }
        }
    }

    public static String getEventStatus(Event event) throws ParseException {
        ZonedDateTime startDate = DateUtils.getDate(event.getStartsAt().get());
        ZonedDateTime endDate = DateUtils.getDate(event.getEndsAt().get());
        ZonedDateTime now = ZonedDateTime.now();
        if (now.isAfter(startDate)) {
            if (now.isBefore(endDate)) {
                return LIVE_EVENT;
            } else {
                return PAST_EVENT;
            }
        } else {
            return UPCOMING_EVENT;
        }
    }
}
