import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getDishes } from 'app/entities/dish/dish.reducer';
import { Status } from 'app/shared/model/enumerations/status.model';
import { PaymentMethods } from 'app/shared/model/enumerations/payment-methods.model';
import { createEntity, getEntity, reset, updateEntity } from './purchase.reducer';

export const PurchaseUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const dishes = useAppSelector(state => state.dish.entities);
  const purchaseEntity = useAppSelector(state => state.purchase.entity);
  const loading = useAppSelector(state => state.purchase.loading);
  const updating = useAppSelector(state => state.purchase.updating);
  const updateSuccess = useAppSelector(state => state.purchase.updateSuccess);
  const statusValues = Object.keys(Status);
  const paymentMethodsValues = Object.keys(PaymentMethods);

  const handleClose = () => {
    navigate(`/purchase${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getDishes({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.ammount !== undefined && typeof values.ammount !== 'number') {
      values.ammount = Number(values.ammount);
    }

    const entity = {
      ...purchaseEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user?.toString()),
      dishes: mapIdList(values.dishes),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          status: 'PENDING',
          paymentMethod: 'CARD',
          ...purchaseEntity,
          user: purchaseEntity?.user?.id,
          dishes: purchaseEntity?.dishes?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="smartdineApp.purchase.home.createOrEditLabel" data-cy="PurchaseCreateUpdateHeading">
            <Translate contentKey="smartdineApp.purchase.home.createOrEditLabel">Create or edit a Purchase</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="purchase-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('smartdineApp.purchase.date')}
                id="purchase-date"
                name="date"
                data-cy="date"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartdineApp.purchase.ammount')}
                id="purchase-ammount"
                name="ammount"
                data-cy="ammount"
                type="text"
              />
              <ValidatedField
                label={translate('smartdineApp.purchase.status')}
                id="purchase-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {statusValues.map(status => (
                  <option value={status} key={status}>
                    {translate(`smartdineApp.Status.${status}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('smartdineApp.purchase.paymentMethod')}
                id="purchase-paymentMethod"
                name="paymentMethod"
                data-cy="paymentMethod"
                type="select"
              >
                {paymentMethodsValues.map(paymentMethods => (
                  <option value={paymentMethods} key={paymentMethods}>
                    {translate(`smartdineApp.PaymentMethods.${paymentMethods}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="purchase-user"
                name="user"
                data-cy="user"
                label={translate('smartdineApp.purchase.user')}
                type="select"
                required
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                label={translate('smartdineApp.purchase.dish')}
                id="purchase-dish"
                data-cy="dish"
                type="select"
                multiple
                name="dishes"
              >
                <option value="" key="0" />
                {dishes
                  ? dishes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/purchase" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PurchaseUpdate;
