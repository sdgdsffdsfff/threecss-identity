package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import action.UCErrorPack;
import action.UserGroupAction;
import dao.model.base.UserGroup;
import http.HOpCodeUCenter;
import http.HSession;
import http.HttpPacket;
import http.IHttpListener;
import http.exception.HttpErrorException;
import protobuf.http.UCErrorProto.UCError;
import protobuf.http.UCErrorProto.UCErrorCode;
import protobuf.http.UserGroupProto.CreateUserGroupC;
import protobuf.http.UserGroupProto.CreateUserGroupS;
import protobuf.http.UserGroupProto.DeleteUserGroupC;
import protobuf.http.UserGroupProto.DeleteUserGroupS;
import protobuf.http.UserGroupProto.GetUserGroupC;
import protobuf.http.UserGroupProto.GetUserGroupListC;
import protobuf.http.UserGroupProto.GetUserGroupListS;
import protobuf.http.UserGroupProto.GetUserGroupS;
import protobuf.http.UserGroupProto.UpdateUserGroupC;
import protobuf.http.UserGroupProto.UpdateUserGroupS;
import tool.PageFormat;
import tool.PageObj;

public class UserGroupService implements IHttpListener {

	@Override
	public Map<Integer, String> getHttps() throws Exception {
		HashMap<Integer, String> map = new HashMap<>();
		map.put(HOpCodeUCenter.CREATE_USER_GROUP, "createUserGroupHandle");
		map.put(HOpCodeUCenter.UPDATE_USER_GROUP, "updateUserGroupHandle");
		map.put(HOpCodeUCenter.GET_USER_GROUP, "getUserGroupHandle");
		map.put(HOpCodeUCenter.DELETE_USER_GROUP, "deleteUserGroupHandle");
		map.put(HOpCodeUCenter.GET_USER_GROUP_LIST, "getUserGroupListHandle");
		return map;
	}

	@Override
	public Object getInstance() {
		return this;
	}

	public HttpPacket createUserGroupHandle(HSession hSession) throws HttpErrorException {
		CreateUserGroupC message = (CreateUserGroupC) hSession.httpPacket.getData();
		UserGroup userGroup = UserGroupAction.createUserGroup(message.getUserGroupName(), message.getUserGroupParentId());
		if (userGroup == null) {
			UCError errorPack = UCErrorPack.create(UCErrorCode.ERROR_CODE_10, hSession.headParam.hOpCode);
			throw new HttpErrorException(HOpCodeUCenter.UC_ERROR, errorPack);
		}
		CreateUserGroupS.Builder builder = CreateUserGroupS.newBuilder();
		builder.setHOpCode(hSession.headParam.hOpCode);
		builder.setUserGroup(UserGroupAction.getUserGroupDataBuilder(userGroup));
		HttpPacket packet = new HttpPacket(hSession.headParam.hOpCode, builder.build());
		return packet;
	}

	public HttpPacket updateUserGroupHandle(HSession hSession) throws HttpErrorException {
		UpdateUserGroupC message = (UpdateUserGroupC) hSession.httpPacket.getData();
		UserGroup userGroup = UserGroupAction.updateUserGroup(message.getUserGroupId(), message.getUserGroupName(), message.getIsUpdateUserGroupParent(), message.getUserGroupParentId(), message.getUserGroupState());
		if (userGroup == null) {
			UCError errorPack = UCErrorPack.create(UCErrorCode.ERROR_CODE_11, hSession.headParam.hOpCode);
			throw new HttpErrorException(HOpCodeUCenter.UC_ERROR, errorPack);
		}
		UpdateUserGroupS.Builder builder = UpdateUserGroupS.newBuilder();
		builder.setHOpCode(hSession.headParam.hOpCode);
		builder.setUserGroup(UserGroupAction.getUserGroupDataBuilder(userGroup));
		HttpPacket packet = new HttpPacket(hSession.headParam.hOpCode, builder.build());
		return packet;
	}

	public HttpPacket getUserGroupHandle(HSession hSession) throws HttpErrorException {
		GetUserGroupC message = (GetUserGroupC) hSession.httpPacket.getData();

		UserGroup userGroup = UserGroupAction.getUserGroupById(message.getUserGroupId());
		if (userGroup == null) {
			UCError errorPack = UCErrorPack.create(UCErrorCode.ERROR_CODE_12, hSession.headParam.hOpCode);
			throw new HttpErrorException(HOpCodeUCenter.UC_ERROR, errorPack);
		}

		GetUserGroupS.Builder builder = GetUserGroupS.newBuilder();
		builder.setHOpCode(hSession.headParam.hOpCode);
		builder.setUserGroup(UserGroupAction.getUserGroupDataBuilder(userGroup));
		HttpPacket packet = new HttpPacket(hSession.headParam.hOpCode, builder.build());
		return packet;
	}

	public HttpPacket deleteUserGroupHandle(HSession hSession) throws HttpErrorException {
		DeleteUserGroupC message = (DeleteUserGroupC) hSession.httpPacket.getData();
		boolean result = UserGroupAction.deleteUserGroup(message.getUserGroupId());
		if (!result) {
			UCError errorPack = UCErrorPack.create(UCErrorCode.ERROR_CODE_15, hSession.headParam.hOpCode);
			throw new HttpErrorException(HOpCodeUCenter.UC_ERROR, errorPack);
		}
		DeleteUserGroupS.Builder builder = DeleteUserGroupS.newBuilder();
		builder.setHOpCode(hSession.headParam.hOpCode);
		HttpPacket packet = new HttpPacket(hSession.headParam.hOpCode, builder.build());
		return packet;
	}

	public HttpPacket getUserGroupListHandle(HSession hSession) throws HttpErrorException {
		GetUserGroupListC message = (GetUserGroupListC) hSession.httpPacket.getData();

		List<UserGroup> userGroupList = UserGroupAction.getUserGroupList(message.getUserGroupParentId(), message.getIsUserGroupParentIsNull(), message.getIsRecursion(), message.getUserGroupTopId(), message.getUserGroupState(), message.getUserGroupCreateTimeGreaterThan(), message.getUserGroupCreateTimeLessThan(), message.getUserGroupUpdateTimeGreaterThan(), message.getUserGroupUpdateTimeLessThan());
		int currentPage = message.getCurrentPage();
		int pageSize = message.getPageSize();
		PageObj pageObj = PageFormat.getStartAndEnd(currentPage, pageSize, userGroupList.size());
		GetUserGroupListS.Builder builder = GetUserGroupListS.newBuilder();
		builder.setHOpCode(hSession.headParam.hOpCode);
		builder.setCurrentPage(pageObj.currentPage);
		builder.setPageSize(pageObj.pageSize);
		builder.setTotalPage(pageObj.totalPage);
		builder.setAllNum(pageObj.allNum);
		if (userGroupList != null) {
			for (int i = pageObj.start; i < pageObj.end; i++) {
				UserGroup userGroup = userGroupList.get(i);
				builder.addUserGroup(UserGroupAction.getUserGroupDataBuilder(userGroup));
			}
		}

		HttpPacket packet = new HttpPacket(hSession.headParam.hOpCode, builder.build());
		return packet;
	}
}
