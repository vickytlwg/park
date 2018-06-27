package com.park.controller;


import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.park.model.*;
import com.park.service.*;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.park.dao.PosChargeDataDAO;

@RequestMapping("barrierCharge")
@Controller
public class BarrierChargeController {
	@Autowired
	HardwareService hardwareService;
	@Autowired
	PosChargeDataService chargeSerivce;
	@Autowired
	PosChargeDataDAO chargeDao;
	@Autowired
	ParkToAliparkService parkToAliparkService;
	@Autowired
	AliParkFeeService aliparkFeeService;
	@Autowired
	MonthUserService monthUserService;
	@Autowired
	AlipayrecordService alipayrecordService;
	@Autowired
	ParkService parkService;
	@Autowired
	FeeCriterionService feeCriterionService;
	@Autowired
	ParkCarAuthorityService parkCarAuthorityService;
	@Autowired
	PosChargeMacService posChargeMacService;
	@Autowired
	SimilarCarNumberService similarCarNumberService;
	
	private static Log logger = LogFactory.getLog(BarrierChargeController.class);

//	@RequestMapping(value = "insert", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
//	@ResponseBody
//	public String insert(@RequestBody Map<String, String> args) throws ParseException, AlipayApiException {
//		String mac = args.get("mac");
//		String cardNumber = args.get("cardNumber");
//		PosChargeData charge = new PosChargeData();
//		Map<String, Object> ret = new HashMap<String, Object>();
//		List<Map<String, Object>> infos = hardwareService.getInfoByMac(mac);
//		Map<String, Object> info = infos.get(0);
//		if (info == null) {
//			ret.put("status", 1002);
//			return Utility.gson.toJson(ret);
//		}
//		Integer parkId = (Integer) info.get("parkID");
//		String parkName = (String) info.get("Name");
//
//		charge.setCardNumber(cardNumber);
//		charge.setParkId(parkId);
//		charge.setParkDesc(parkName);
//		charge.setEntranceDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//		int num = chargeSerivce.insert(charge);
//		if (num == 1) {
//			List<Parktoalipark> parktoaliparks = parkToAliparkService.getByParkId(parkId);
//			if (!parktoaliparks.isEmpty()) {
//				Parktoalipark parktoalipark = parktoaliparks.get(0);
//				Map<String, String> argstoali = new HashMap<>();
//				argstoali.put("parking_id", parktoalipark.getAliparkingid());
//				argstoali.put("car_number", cardNumber);
//				argstoali.put("in_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//				aliparkFeeService.parkingEnterinfoSync(argstoali);
//			}
//			ret.put("status", 1001);
//		} else {
//			ret.put("status", 1002);
//		}
//		return Utility.gson.toJson(ret);
//	}
//
	@RequestMapping(value = "exit", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String exit(@RequestBody Map<String, Object> args) throws ParseException {
		String cardNumber = (String) args.get("cardNumber");
		List<PosChargeData> queryCharges = null;
		String exitDate = (String) args.get("exitDate");
		if (exitDate != null) {
			Date eDate = new SimpleDateFormat(Constants.DATEFORMAT).parse(exitDate);
			try {
				queryCharges = chargeSerivce.getDebt(cardNumber, eDate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Utility.createJsonMsg(1002, e);
			}
		} else {
			try {
				queryCharges = chargeSerivce.getDebt(cardNumber);
			} catch (Exception e) {
				return Utility.createJsonMsg(1002, e);
			}
		}
		PosChargeData payRet = queryCharges.get(0);
		payRet.setPaidCompleted(true);
		payRet.setPaidMoney(payRet.getChargeMoney());
		payRet.setUnPaidMoney(0);
		payRet.setOperatorId("道闸");
		int num = chargeSerivce.update(payRet);
		if (num == 1) {
			return Utility.createJsonMsg(1001, "success", payRet);
		} else {
			return Utility.createJsonMsg(1002, "fail");
		}
	}

	@RequestMapping(value = "touchtest", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String touchtest(@RequestBody String aString){
		return aString;
	}
	@RequestMapping(value = "touched", method = RequestMethod.POST, produces = { "application/json;charset=utf-8" })
	@ResponseBody
	public String touched(@RequestBody Map<String, String> args) throws Exception {
		String mac = args.get("mac");
		String cardNumber = args.get("cardNumber");
		logger.info("touch车辆" + cardNumber);
		
			
		
		boolean largeCar = Boolean.parseBoolean(args.get("largeCar"));
		PosChargeData charge = new PosChargeData();
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> dataMap = new TreeMap<String, Object>();
		List<Map<String, Object>> infos = hardwareService.getInfoByMac(mac);
		if (infos.isEmpty()) {
			dataMap.put("status", 1003);
			return Utility.gson.toJson(dataMap);
		}
		Map<String, Object> info = infos.get(0);
		if (info == null) {
			return Utility.createJsonMsg(1002, "fail");
		}
		int channelFlag = (int) info.get("channelFlag");
		Integer parkId = (Integer) info.get("parkID");
		//相似车牌管理
		List<Similarcarnumber> similarcarnumbers=similarCarNumberService.selectBySimilarCarNumberAndPark(cardNumber, parkId);
		if (!similarcarnumbers.isEmpty()) {
			cardNumber=similarcarnumbers.get(0).getRealnumber();
			logger.info("更正车牌:" + cardNumber);
		}
		
		List<Monthuser> monthusers = monthUserService.getByCarnumberAndPark(cardNumber, parkId);
		Park park = parkService.getParkById(parkId);
		List<Parkcarauthority> parkcarauthorities = parkCarAuthorityService.getByParkId(parkId);
		if (parkcarauthorities.isEmpty()) {
			return null;
		}

		dataMap.put("aT", "1");
		boolean isMonthUser = false;
		boolean isRealMonthUser = false;
		int monthUserType = 9;
		Monthuser monthuserNow = null;
		if (!monthusers.isEmpty()) {
			isMonthUser = true;
			for (Monthuser tmpMonthuser : monthusers) {
				Long diff = (tmpMonthuser.getEndtime().getTime() - (new Date()).getTime());
				monthuserNow = tmpMonthuser;
				if (tmpMonthuser.getType() == 0) {//月卡
					monthUserType = 0;
					if (diff > 0) {
						int leftDays = (int) (diff / (1000 * 60 * 60 * 24));
						dataMap.put("ds", String.valueOf(leftDays));
						isRealMonthUser = true;
						dataMap.put("eD", new SimpleDateFormat(Constants.DATEFORMAT).format(tmpMonthuser.getEndtime()));
						break;
					} else {
						dataMap.put("ds", "-1");
						monthUserType = 8; //月卡过期
					}

				} else {
					if (diff >= 0) {
						monthUserType = tmpMonthuser.getType();
						break;
					} else {
						isMonthUser = false;
					}
				}
			}

		}
		
		
		
		
		if (!isMonthUser) {
			dataMap.put("uT", "0");
		} else {
			// Monthuser monthuser=monthusers.get(0);
			dataMap.put("uT", "1");
			// 判断是否是预约车
			if (monthUserType != 0 && monthUserType != 9) {
				dataMap.put("uT", "2");
			}
		}
		List<Parktoalipark> parktoaliparks = parkToAliparkService.getByParkId(parkId);
		Boolean isMultiCarsOneCarport=false;
		if (park.getDescription()!=null&&park.getDescription().contains("一位多车")) {
			logger.info(cardNumber+"进入一位多车停车场");
			isMultiCarsOneCarport=true;
		}
		Boolean isMultiFeeCtriterion=false;
		if (park.getDescription().contains("多计费标准")) {
			isMultiFeeCtriterion=true;
		}
		// 判断是否有多个车
		List<Monthuser> realMonthUsers = new ArrayList<>();
		if (monthuserNow != null && monthuserNow.getCardnumber() != null&& !monthuserNow.getCardnumber().equals("") ) {
			List<Monthuser> monthuserss=monthUserService.getByParkAndPort(monthuserNow.getParkid(), monthuserNow.getCardnumber());									 
			for (Monthuser monthuser : monthuserss) {
				if (monthuser.getType().intValue() == 0) {
					realMonthUsers.add(monthuser);
				}
			}
		}

		// 入口硬件
		if (channelFlag == 1) {
			if (largeCar == true) {
				charge.setIsLargeCar(true);
			}

			// 取得入口权限
			Parkcarauthority parkcarauthority = parkcarauthorities.get(0);
			for (Parkcarauthority tmParkcarauthority : parkcarauthorities) {
				if (tmParkcarauthority.getChannel() == 1) {
					parkcarauthority = tmParkcarauthority;
					break;
				}
			}
			switch (monthUserType) {
			case 0:
				if (parkcarauthority.getMonth() != true) {
					dataMap.put("aT", "0");
				}
				break;
			case 1:
				if (parkcarauthority.getTypea() != true) {
					dataMap.put("aT", "0");
				}
				break;
			case 2:
				if (parkcarauthority.getTypeb() != true) {
					dataMap.put("aT", "0");
				}
				break;
			case 3:
				if (parkcarauthority.getTypec() != true) {
					dataMap.put("aT", "0");
				}
				break;
			case 4:
				if (parkcarauthority.getTyped() != true) {
					dataMap.put("aT", "0");
				}
				break;
			case 8:
				if (parkcarauthority.getMonthexpired() != true) {
					dataMap.put("aT", "0");
				}
				break;	
			case 9:
				if (parkcarauthority.getTemporary() != true) {
					dataMap.put("aT", "0");
				}
				break;
			default:
				break;
			}

			dataMap.put("cT", "in");
			charge.setCardNumber(cardNumber);
			charge.setParkId(parkId);
			charge.setParkDesc(park.getName() + "-临停车");
			if (isRealMonthUser) {
				charge.setParkDesc(park.getName() + "-包月车");
			}
			// 判断包月车已入场

			Boolean isMonthUserCarIn = false;
			if (isMultiCarsOneCarport&&isRealMonthUser && realMonthUsers.size() > 1) {
				for (Monthuser tmMonthuser : realMonthUsers) {
					if (tmMonthuser.getPlatecolor().equals("多车包月入场")
							|| tmMonthuser.getPlatecolor().equals("临停恢复为包月")) {
						if (!tmMonthuser.getPlatenumber().equals(cardNumber)) {
							isMonthUserCarIn = true;
						}
						
					}
				}
				monthuserNow.setPlatecolor("多车包月入场");
				if (isMonthUserCarIn) {
					monthuserNow.setPlatecolor("包月转为临停");
					dataMap.put("uT", "0");
					try {
						dataMap.remove("ds");
						dataMap.remove("eD");
					} catch (Exception e) {
						// TODO: handle exception
					}
					
				}
				monthUserService.updateByPrimaryKeySelective(monthuserNow);
			}

			if (monthUserType >= 1 && monthUserType <= 5) {
				charge.setParkDesc(park.getName() + "-预约车");
			}
			charge.setEntranceDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			int num = chargeSerivce.insert(charge);
			if (num == 1) {
				Poschargemac poschargemac=new Poschargemac();
				poschargemac.setMacidenter((int)info.get("macId"));
				poschargemac.setPoschargeid(charge.getId());
				posChargeMacService.insertSelective(poschargemac);
				try {
					Map<String, Object> argMap = new HashMap<>();
					FeeCriterion feeCriterion = feeCriterionService.getById(park.getFeeCriterionId());
					argMap.put("parkId", parkId);
					argMap.put("plateNum", charge.getCardNumber());
					argMap.put("enterTime",
							new SimpleDateFormat(Constants.DATEFORMAT).format(charge.getEntranceDate()));
					argMap.put("chargeStandard", feeCriterion.getExplaination());

					if (!parktoaliparks.isEmpty()) {
						Parktoalipark parktoalipark = parktoaliparks.get(0);
						Map<String, String> argstoali = new HashMap<>();
						argstoali.put("parking_id", parktoalipark.getAliparkingid());
						argstoali.put("car_number", cardNumber);
						argstoali.put("in_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						ActiveMqService.SendWithQueueName(JsonUtils.objectToJson(argstoali), "aliEnterInfo");
					//	aliparkFeeService.parkingEnterinfoSync(argstoali);
					} else {
//						Map<String, String> argstoali = new HashMap<>();
//						argstoali.put("parking_id", "PI1501317472942184881");
//						argstoali.put("car_number", cardNumber);
//						argstoali.put("in_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						// aliparkFeeService.parkingEnterinfoSync(argstoali);
					}

				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e);
				}
				logger.info(cardNumber + "入场成功!" + dataMap.toString());
				ret.put("status", 1001);
			} else {
				ret.put("status", 1002);
			}
			return Utility.createJsonMsgWithoutMsg(1001, dataMap);
		} else { // 出场硬件
			dataMap.put("cT", "out");
			List<PosChargeData> queryCharges = null;
			String exitDate = (String) args.get("exitDate");
			logger.info(cardNumber + "开始出场");

			// 取得出口权限
			Parkcarauthority parkcarauthority = parkcarauthorities.get(0);
			for (Parkcarauthority tmParkcarauthority : parkcarauthorities) {
				if (tmParkcarauthority.getChannel() == 0) {
					parkcarauthority = tmParkcarauthority;
					break;
				}
			}
			switch (monthUserType) {
			case 0:
				if (parkcarauthority.getMonth() != true) {
					dataMap.put("aT", "0");
				}
				break;
			case 1:
				if (parkcarauthority.getTypea() != true) {
					dataMap.put("aT", "0");
				}
				break;
			case 2:
				if (parkcarauthority.getTypeb() != true) {
					dataMap.put("aT", "0");
				}
				break;
			case 3:
				if (parkcarauthority.getTypec() != true) {
					dataMap.put("aT", "0");
				}
				break;
			case 4:
				if (parkcarauthority.getTyped() != true) {
					dataMap.put("aT", "0");
				}
				break;
			case 8:
				if (parkcarauthority.getMonthexpired() != true) {
					dataMap.put("aT", "0");
				}
				break;	
			case 9:
				if (parkcarauthority.getTemporary() != true) {
					dataMap.put("aT", "0");
				}
				break;
			default:
				break;
			}
			
			if (exitDate != null) {
				Date eDate = new SimpleDateFormat(Constants.DATEFORMAT).parse(exitDate);
				try {
					// System.out.println("出场时间为空,将要进行getDebt计算: "+new
					// Date().getTime()+"\n");
				//	queryCharges = chargeSerivce.getDebt(cardNumber, eDate);
					queryCharges = chargeSerivce.getDebtWithData(cardNumber, parktoaliparks, realMonthUsers, park,isMultiFeeCtriterion,monthUserType);

					// System.out.println("出场时间为空,getDebt计算完毕: "+new
					// Date().getTime()+"\n");

				} catch (Exception e) {
					// TODO Auto-generated catch block
					return Utility.createJsonMsg(1002, "获取费用出现异常");
				}
			} else {
				try {
					// System.out.println("出场时间不为空,将要进行getDebt计算: "+new
					// Date().getTime()+"\n");
					queryCharges = chargeSerivce.getDebtWithData(cardNumber, parktoaliparks, realMonthUsers, park,isMultiFeeCtriterion,monthUserType);
					// System.out.println("出场时间不为空,getDebt计算完毕: "+new
					// Date().getTime()+"\n");
				} catch (Exception e) {
					return Utility.createJsonMsg(1002, e);
				}
			}
			logger.info(cardNumber + "计费结束!");

			// 如果没有未缴费 判断最近一次缴费时间是否超过15分钟
			if (queryCharges.isEmpty()) {
				logger.info(cardNumber + "没有未缴费!");
				FeeCriterion feeCriterion = feeCriterionService.getById(park.getFeeCriterionId());
				// if (largeCar==true) {
				// charge.setIsLargeCar(true);
				// }
				// charge.setCardNumber(cardNumber);
				// charge.setParkId(parkId);
				// charge.setParkDesc(parkName);
				// charge.setEntranceDate(new SimpleDateFormat("yyyy-MM-dd
				// HH:mm:ss").format(new Date()));
				// chargeSerivce.insert(charge);
				/*
				 * if (!parktoaliparks.isEmpty()) { Parktoalipark
				 * parktoalipark=parktoaliparks.get(0); Map<String, String>
				 * argstoali=new HashMap<>(); argstoali.put("parking_id",
				 * parktoalipark.getAliparkingid()); argstoali.put("car_number",
				 * cardNumber); argstoali.put("out_time", new SimpleDateFormat(
				 * "yyyy-MM-dd HH:mm:ss").format(new Date()));
				 * System.out.println("支付宝同步出场计算前: "+new Date().getTime()+"\n");
				 * aliparkFeeService.parkingExitinfoSync(argstoali);
				 * System.out.println("支付宝同步出场计算后: "+new Date().getTime()+"\n");
				 * }
				 */
				if (isRealMonthUser&&realMonthUsers.size()==1) {
					// dataMap.put("my", "0.0");
					return Utility.createJsonMsgWithoutMsg(1001, dataMap);
				}
				// 以下就是查询停车费状态的部分迁移
				List<PosChargeData> posChargeDataList = chargeSerivce.getLastRecordWithPark(cardNumber, 1, parkId);
				if (posChargeDataList.isEmpty()) {
					dataMap.put("my", "0.0");
					return Utility.createJsonMsgWithoutMsg(1001, dataMap);
				}
				PosChargeData posChargeData = posChargeDataList.get(0);
				Date payDate = new Date();
				if (posChargeData.getPayType() == 0) {
					List<Alipayrecord> alipayrecords = alipayrecordService.getByPosChargeId(posChargeData.getId());
					if (alipayrecords.isEmpty()) {
						// dataMap.put("my", "0.0");
						return Utility.createJsonMsgWithoutMsg(1003, dataMap);
					}
					payDate = alipayrecords.get(0).getDate();
				} else if (posChargeData.getPayType() == 1) {
					payDate = posChargeData.getExitDate();
				}
				if (payDate == null) {
					payDate = new Date();
				}
				long diff = new Date().getTime() - payDate.getTime();
				if (diff < 1000 * 60 * 15) {
					dataMap.put("my", "0.0");
					long diff1 = (new Date().getTime() - posChargeData.getEntranceDate().getTime());
					dataMap.put("eD", String.valueOf(diff1 / (1000 * 60)));
					posChargeData.setPaidCompleted(true);
					// posChargeData.setPayType(9);
					posChargeData.setPaidMoney(posChargeData.getChargeMoney());
					posChargeData.setUnPaidMoney(0);
					if (posChargeData.getExitDate()==null) {
						posChargeData.setExitDate1(new Date());
					}
					posChargeData.setOperatorId("道闸15min");
					chargeSerivce.update(posChargeData);
					//发送到队列
					if (ArrayUtils.contains(Constants.parkToQuene, parkId.intValue())) {
						ActiveMqService.SendPosChargeData(JsonUtils.objectToJson(posChargeData));
					}
					return Utility.createJsonMsgWithoutMsg(1001, dataMap);
				}
				// 超过了15分钟
				else {
					// Park
					// park=parkService.getParkById(posChargeData.getParkId());
					// FeeCriterion
					// feeCriterion=feeCriterionService.getById(park.getFeeCriterionId());
					Date incomeDate = new Date(payDate.getTime() - (long)(feeCriterion.getFreemins() - 15) * 1000 * 60);
					PosChargeData charge2 = new PosChargeData();
					charge2.setCardNumber(posChargeData.getCardNumber());
					charge2.setParkId(park.getId());
					charge2.setOperatorId("超时15分钟");
					charge2.setParkDesc(posChargeData.getParkDesc());
					charge2.setEntranceDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(incomeDate));
					int num = chargeSerivce.insert(charge2);
					if (num != 1) {
						return Utility.createJsonMsgWithoutMsg(1003, dataMap);
					}
					// 重新查询未缴费
					queryCharges = chargeSerivce.getDebt(cardNumber);
				}
			}
			PosChargeData payRet = new PosChargeData();
			int tmpnn = 0;
			logger.info(cardNumber + "计费完毕后处理");
			int num=0;
			if (!queryCharges.isEmpty()) {
				for (PosChargeData posChargeData : queryCharges) {
					if (posChargeData.getParkId() == parkId.intValue()) {
						posChargeData.setPaidCompleted(true);
						posChargeData.setPaidMoney(posChargeData.getChargeMoney());
						posChargeData.setUnPaidMoney(0);
						posChargeData.setPayType(9);
						posChargeData.setOperatorId("道闸");
						if (tmpnn == 0) {
							payRet = posChargeData;
							tmpnn++;
						} else {
							posChargeData.setChargeMoney(0);
							posChargeData.setPaidMoney(0);
						}
						chargeSerivce.update(posChargeData);
					} else {
						posChargeData.setChargeMoney(0.0);
						posChargeData.setPaidCompleted(true);
						posChargeData.setPayType(9);
						chargeSerivce.update(posChargeData);
					}
				}
//				if (payRet.getExitDate()==null) {
//					payRet.setExitDate1(new Date());
//				}
				num = chargeSerivce.update(payRet);
			}
			else {
				logger.info(cardNumber + "查询计费结果为空!!!");
				return Utility.createJsonMsg(1002, "计费结果为空");
			}
			
			//发送到队列
			if (ArrayUtils.contains(Constants.parkToQuene, parkId.intValue())) {
				ActiveMqService.SendPosChargeData(JsonUtils.objectToJson(payRet));
			}
			
			if (payRet.getEntranceDate()==null) {
				
				return Utility.createJsonMsg(1003, "无入场记录");
			}
			
			long diff2 = (new Date().getTime() - payRet.getEntranceDate().getTime());
			dataMap.put("eD", String.valueOf(diff2 / (60 * 1000)));
			if (!isRealMonthUser) {
				dataMap.put("my", String.valueOf(payRet.getChargeMoney()));
			}
			if (isMultiCarsOneCarport&&isRealMonthUser&&realMonthUsers.size()>1) {
				if (payRet.getChargeMoney()>0) {
					dataMap.put("my", String.valueOf(payRet.getChargeMoney()));
					dataMap.put("uT", "0");
				}
			}
			if (num == 1) {
				Poschargemac poschargemac=new Poschargemac();
				poschargemac.setMacidout((int)info.get("macId"));
				poschargemac.setPoschargeid(payRet.getId());
				posChargeMacService.updateByPosChargeId(poschargemac); 
				
				logger.info(cardNumber + "出场成功!" + dataMap.toString());
				return Utility.createJsonMsgWithoutMsg(1001, dataMap);
			} else {
				return Utility.createJsonMsg(1001, "ok");
			}
		}
	}

	@RequestMapping(value = "touchedNotify", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public String touchedNotify(@RequestBody Map<String, String> args) throws Exception {
		String mac = args.get("mac");
		String cardNumber = args.get("cardNumber");
		boolean largeCar = Boolean.parseBoolean(args.get("largeCar"));
		PosChargeData charge = new PosChargeData();
		charge.setEntranceDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		Map<String, Object> ret = new HashMap<String, Object>();
		List<Map<String, Object>> infos = hardwareService.getInfoByMac(mac);
		Map<String, Object> info = infos.get(0);
		if (info == null) {
			return Utility.createJsonMsg(1002, "fail");
		}
		// Map<String, Object> dataMap = new TreeMap<String, Object>();
		int channelFlag = (int) info.get("channelFlag");
		Integer parkId = (Integer) info.get("parkID");
		// String parkName=(String) info.get("Name");
		List<Monthuser> monthusers = monthUserService.getByCarnumberAndPark(cardNumber, parkId);
		Park park = parkService.getParkById(parkId);
		if (channelFlag == 1) {
			Map<String, Object> argMap = new HashMap<>();
			FeeCriterion feeCriterion = feeCriterionService.getById(park.getFeeCriterionId());
			argMap.put("parkId", "bd1879d2-bc89-48e5-9e46-927d0f4ec56f");
			argMap.put("plateNum", cardNumber);
			argMap.put("enterTime", new SimpleDateFormat(Constants.DATEFORMAT).format(charge.getEntranceDate()));
			argMap.put("chargeStandard", feeCriterion.getExplaination());
			logger.info("调用贵州入场接口..参数" + argMap.toString());
			try {
				Map<String, Object> infoReturn = HttpUtil
						.post("https://www.gkzhxxsj.com/parking/api/device/barrier/enterCar", argMap);
				logger.info("调用贵州入场接口..返回结果" + infoReturn.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else {
			Map<String, Object> argMap = new HashMap<>();
			FeeCriterion feeCriterion = feeCriterionService.getById(park.getFeeCriterionId());
			try {

				argMap.put("parkId", "bd1879d2-bc89-48e5-9e46-927d0f4ec56f");
				argMap.put("plateNum", cardNumber);
				argMap.put("leaveTime", new SimpleDateFormat(Constants.DATEFORMAT).format(charge.getEntranceDate()));
				// argMap.put("chargeStandard", feeCriterion.getExplaination());
				logger.info("调用贵州出场接口..参数" + argMap.toString());
				Map<String, Object> infoReturn = HttpUtil
						.post("https://www.gkzhxxsj.com/parking/api/device/barrier/leaveCar", argMap);
				logger.info("调用贵州出场接口..结果" + infoReturn.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return Utility.createJsonMsg(1001, "ok");
	}

	public String getTimeDiff(Long diff) {
		long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
		long nh = 1000 * 60 * 60;// 一小时的毫秒数
		long nm = 1000 * 60;// 一分钟的毫秒数
		long ns = 1000;// 一秒钟的毫秒数

		long day = diff / nd;// 计算差多少天
		long hour = diff % nd / nh;// 计算差多少小时
		long min = diff % nd % nh / nm;// 计算差多少分钟
		long sec = diff % nd % nh % nm / ns;// 计算差多少秒//输出结果

		if (day != 0) {
			return day + "天" + hour + "小时" + min + "分钟";
		}
		if (day == 0 && hour != 0) {
			return hour + "小时" + min + "分钟";
		}
		if (day == 0 && hour == 0) {
			return min + "分钟";
		}
		return day + "天" + hour + "小时" + min + "分钟";
	}

	@RequestMapping(value = "getTypeByMac", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public String getTypeByMac(@RequestBody Map<String, String> args) {
		String mac = args.get("mac");
		List<Map<String, Object>> infos = hardwareService.getInfoByMac(mac);
		if (infos.isEmpty()) {
			return Utility.createJsonMsg(1002, "fail");
		}
		Map<String, Object> info = infos.get(0);
		Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
		int channelFlag = (int) info.get("channelFlag");
		if (channelFlag == 1) {
			dataMap.put("channelType", "in");
		} else {
			dataMap.put("channelType", "out");
		}
		dataMap.put("time", new SimpleDateFormat(Constants.DATEFORMAT).format(new Date()));
		return Utility.createJsonMsg(1001, "success", dataMap);
	}

	/***
	 * 查询是否已缴费接口
	 * 
	 * @throws ParseException
	 *
	 ***/
	@RequestMapping(value = "queryChargeStatus", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public String queryChargeStatus(@RequestBody Map<String, String> args) throws ParseException {
		String carNumber = args.get("carNumber");
		Map<String, Object> ret = new HashMap<String, Object>();
		List<PosChargeData> posChargeDataList = chargeSerivce.getLastRecord(carNumber, 1);
		if (posChargeDataList.isEmpty()) {
			ret.put("status", 1002);
			ret.put("message", "没有停车记录!");
			return Utility.gson.toJson(ret);
		}
		PosChargeData posChargeData = posChargeDataList.get(0);
		if (!posChargeData.isPaidCompleted()) {
			ret.put("status", 1002);
			ret.put("message", "未完成支付!");
			ret.put("body", posChargeData);
			return Utility.gson.toJson(ret);
		}
		int payType = posChargeData.getPayType();
		if (payType == 0) {
			List<Alipayrecord> alipayrecords = alipayrecordService.getByPosChargeId(posChargeData.getId());
			if (alipayrecords.isEmpty()) {
				ret.put("status", 1002);
				ret.put("message", "非支付宝支付!");
				return Utility.gson.toJson(ret);
			}
			Date payDate = alipayrecords.get(0).getDate();
			long diff = new Date().getTime() - payDate.getTime();
			if (diff < 1000 * 60 * 15) {
				ret.put("status", 1001);
				ret.put("message", "已完成支付!");
				return Utility.gson.toJson(ret);
			} else {
				Park park = parkService.getParkById(posChargeData.getParkId());
				FeeCriterion feeCriterion = feeCriterionService.getById(park.getFeeCriterionId());
				Date incomeDate = new Date(payDate.getTime() - feeCriterion.getFreemins() * 1000 * 60);
				PosChargeData charge = new PosChargeData();
				charge.setCardNumber(posChargeData.getCardNumber());
				charge.setParkId(park.getId());
				charge.setParkDesc(posChargeData.getParkDesc());
				charge.setEntranceDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(incomeDate));
				int num = chargeSerivce.insert(charge);
				ret.put("status", 1002);
				ret.put("message", "超时出场,还有未完成支付!");
				ret.put("body", charge);
				return Utility.gson.toJson(ret);
			}
		} else {
			List<Alipayrecord> alipayrecords = alipayrecordService.getByPosChargeId(posChargeData.getId());
			if (alipayrecords.isEmpty()) {
				ret.put("status", 1002);
				ret.put("message", "非支付宝支付!");
				return Utility.gson.toJson(ret);
			}
			Date payDate = alipayrecords.get(0).getDate();
			long diff = new Date().getTime() - payDate.getTime();
			if (diff < 1000 * 60 * 15) {
				ret.put("status", 1001);
				ret.put("message", "已完成支付!");
				return Utility.gson.toJson(ret);
			} else {
				Park park = parkService.getParkById(posChargeData.getParkId());
				FeeCriterion feeCriterion = feeCriterionService.getById(park.getFeeCriterionId());
				Date incomeDate = new Date(payDate.getTime() - feeCriterion.getFreemins() * 1000 * 60);
				PosChargeData charge = new PosChargeData();
				charge.setCardNumber(posChargeData.getCardNumber());
				charge.setParkId(park.getId());
				charge.setParkDesc(posChargeData.getParkDesc());
				charge.setEntranceDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(incomeDate));
				int num = chargeSerivce.insert(charge);
				ret.put("status", 1002);
				ret.put("message", "超时出场,还有未完成支付!");
				ret.put("body", charge);
				return Utility.gson.toJson(ret);
			}
		}
	}
}
