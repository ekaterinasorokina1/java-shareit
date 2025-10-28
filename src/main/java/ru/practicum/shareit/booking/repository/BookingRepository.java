package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StateEnum;
import ru.practicum.shareit.booking.model.StatusEnum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends CrudRepository<Booking, Long> {
    @Query("select b from Booking b " +
            "where b.id = ?1 and (b.booker.id = ?2 or b.item.ownerId = ?2)"
    )
    Optional<Booking> findByUserIdOrBookerId(Long bookingId, Long userId);

    Optional<Booking> findFirstByItemIdAndBookerIdAndStatusAndEndBefore(Long itemId, Long userId, StatusEnum status, LocalDateTime date);

    List<Booking> findByBookerIdOrderByStartDate(Long bookerId);

    List<Booking> findByBookerIdAndStartDateBeforeAndEndDateAfterOrderByStartDate(Long bookerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findByBookerIdAndEndDateBeforeOrderByEndDate(Long bookerId, LocalDateTime end);

    List<Booking> findByBookerIdAndStartDateAfterOrderByStartDate(Long bookerId, LocalDateTime end);

    List<Booking> findByBookerIdAndStatusOrderByStartDate(Long bookerId, StateEnum status);

    List<Booking> findByItemOwnerIdOrderByStartDate(Long bookerId);

    List<Booking> findByItemOwnerIdAndStartDateBeforeAndEndDateAfterOrderByStartDate(Long bookerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findByItemOwnerIdAndEndDateBeforeOrderByEndDate(Long bookerId, LocalDateTime end);

    List<Booking> findByItemOwnerIdAndStartDateAfterOrderByStartDate(Long bookerId, LocalDateTime end);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDate(Long bookerId, StateEnum status);

    List<Booking> findByItemIdAndEndDateBeforeOrderByEndDateDesc(Long itemId, LocalDateTime date);

    List<Booking> findByItemIdAndStartDateAfterOrderByStartDate(Long itemId, LocalDateTime date);
}