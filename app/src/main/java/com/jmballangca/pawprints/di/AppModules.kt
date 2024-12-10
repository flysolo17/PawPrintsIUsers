package com.jmballangca.pawprints.di


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.jmballangca.pawprints.repository.appointment.AppointmentRepository

import com.jmballangca.pawprints.repository.appointment.AppointmentRepositoryImpl
import com.jmballangca.pawprints.repository.auth.AuthRepository
import com.jmballangca.pawprints.repository.auth.AuthRepositoryImpl
import com.jmballangca.pawprints.repository.cart.CartRepository
import com.jmballangca.pawprints.repository.cart.CartRepositoryImpl
import com.jmballangca.pawprints.repository.inbox.InboxRepository
import com.jmballangca.pawprints.repository.inbox.InboxRepositoryImpl
import com.jmballangca.pawprints.repository.messaging.MessageRepository
import com.jmballangca.pawprints.repository.messaging.MessageRepositoryImpl
import com.jmballangca.pawprints.repository.pets.PetRepository
import com.jmballangca.pawprints.repository.pets.PetRepositoryImpl
import com.jmballangca.pawprints.repository.products.ProductRepository
import com.jmballangca.pawprints.repository.products.ProductRepositoryImpl
import com.jmballangca.pawprints.repository.schedule.ScheduleRepository
import com.jmballangca.pawprints.repository.schedule.ScheduleRepositoryImpl
import com.jmballangca.pawprints.repository.transactions.TransactionRepository
import com.jmballangca.pawprints.repository.transactions.TransactionRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
        storage : FirebaseStorage,
    ): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth, firestore, storage)
    }

    @Provides
    @Singleton
    fun provideProductRepository(
        firestore: FirebaseFirestore
    ) : ProductRepository  {
        return  ProductRepositoryImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideCartRepository(
        firestore: FirebaseFirestore
    ) : CartRepository  {
        return  CartRepositoryImpl(firestore)
    }


    @Provides
    @Singleton
    fun provideScheduleRepository(
        firestore: FirebaseFirestore
    ) : ScheduleRepository  {
        return  ScheduleRepositoryImpl(firestore)
    }


    @Provides
    @Singleton
    fun providePetRepository(
        firestore: FirebaseFirestore,
        storage: FirebaseStorage
    )  : PetRepository {
        return PetRepositoryImpl(firestore,storage)
    }

    @Provides
    @Singleton
    fun provideAppointmentRepository(
        firestore: FirebaseFirestore,

    )  : AppointmentRepository {
        return AppointmentRepositoryImpl(firestore)
    }



    @Provides
    @Singleton
    fun provideInboxRepository(
        firestore: FirebaseFirestore,
        )  : InboxRepository {
        return InboxRepositoryImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(
        firestore: FirebaseFirestore,
    )  : TransactionRepository {
        return TransactionRepositoryImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideMessageRepository(
        firestore: FirebaseFirestore,
    )  : MessageRepository {
        return MessageRepositoryImpl(firestore)
    }
}