package com.github.sashavasko.batch

import groovy.util.logging.Log4j

@Log4j
class BatchRunner {

	static main(args) {
		
		int result = 0
		
		def batchClassName = "com.github.sashavasko.batch.${args[0]}"
		def cls = batchClassName as Class
		def batch = cls.newInstance()
		// TODO: Add StopWatch and commons.lang
		try{
			log.info("Launching class $batchClassName")
			batch.run(args.drop(1))
		} catch (Throwable e) {
		   e.printStackTrace()
		   result = 1
		}
		log.info("Finished running class $batchClassName")
		result
	}
}
