/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.recover;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.dell.acs.managers.DataImportManager.FileStatus;
import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.persistence.repository.DataFileRepository;
import com.sourcen.core.config.ConfigurationService;

/**
 * @author Shawn R Fisk.
 * @author $LastChangedBy
 * @version $Revision
 */
public final class FeedPreprocessorRecoverTask implements RecoverTask {
	private static AtomicBoolean running = new AtomicBoolean(false);

	/**
     * logger
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());
	private int _status;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Transactional
	@Override
	public void run() {
		this._status = FeedPreprocessorRecoverTask.SUCCESSFUL;
		if (!running.get()) {
			running.set(true);

			try {
				Collection<DataFile> dataFiles = this.dataFileRepository
						.getRecoverPreprocessFiles(configurationService
								.getApplicationUrl());

				for (DataFile dataFile : dataFiles) {
					dataFile.setHost(null);
					dataFile.setStatus(FileStatus.PREPROCESS_QUEUE);
					this.dataFileRepository.update(dataFile);
				}
			} catch (Exception e) {
				logger.warn(e.getMessage() != null ? e.getMessage()
						: "Null pointer exception", e);
				this._status = FeedPreprocessorRecoverTask.ERROR;
			}

			running.set(false);
		} else {
			logger.warn("Recover feed preprocessor job is already running, skip!");
			this._status = FeedPreprocessorRecoverTask.ALREADY_RUNNING;
		}
	}

    /**
     * ConfigurationService bean injection.
     */
    @Autowired
    protected ConfigurationService configurationService;

    public void setConfigurationService(final ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

	/**
	 * DataFileRepository bean injection.
	 */
	@Autowired
	private DataFileRepository dataFileRepository;

	/**
	 * Setter for dataFileRepository
	 */
	public void setDataFileRepository(
			final DataFileRepository dataFileRepository) {
		this.dataFileRepository = dataFileRepository;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
	}

	/*
	 * (non-Javadoc)
	 * @see com.dell.acs.recover.RecoverTask#getStatus()
	 */
	@Override
	public int getStatus() {
		return this._status;
	}
}
