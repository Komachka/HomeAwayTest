package com.kstor.homeawaytest.di
import com.kstor.homeawaytest.domain.StaticMapRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestStaticMapRepositoryModule(private val testRepository: StaticMapRepository) {
    @Provides
    @Singleton
    fun provideStaticMapRepository(): StaticMapRepository = testRepository
}
