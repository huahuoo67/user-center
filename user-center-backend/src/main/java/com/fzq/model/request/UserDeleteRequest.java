package com.fzq.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserDeleteRequest implements Serializable {

	@Serial
	private static final long serialVersionUID = 3256613822892530842L;

	private Long id;

}
