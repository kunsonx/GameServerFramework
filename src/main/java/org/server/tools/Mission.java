/**
 * Copyright 2015年11月20日 Hxms.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
package org.server.tools;

import java.util.concurrent.Phaser;

/**
 * 
 * 任务同步器
 * 
 * @author Hxms
 *
 */
public class Mission {

	Phaser phaser = new Phaser(1);
	int phaserCount = -1;

	/**
	 * 创建新任务
	 * 
	 * @return 任务代码
	 */
	public int newMission() {
		phaserCount = phaser.getPhase();
		return phaserCount;
	}

	/**
	 * 发送任务结束，导致等待继续
	 */
	public boolean missionFinish() {
		if (phaser.getPhase() <= phaserCount) {
			phaser.arrive();
			return true;
		}
		return false;
	}

	/**
	 * 获得当前任务代码
	 * 
	 * @return 当前任务代码
	 */
	public int getMissionCode() {
		return phaserCount;
	}

	/**
	 * 等待任务完成
	 */
	public void awaitMission() {
		phaser.awaitAdvance(getMissionCode());
	}
}
